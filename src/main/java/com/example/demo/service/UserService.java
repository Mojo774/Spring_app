package com.example.demo.service;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailSender mailSender;
    @Qualifier("getPasswordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);


        if (user == null) {
            throw new UsernameNotFoundException(username);
        }


        return user;

    }

    private void sendMessage(String email, String message) {
        if (!StringUtils.isEmpty(email)) {
            // Ссылку можно вынести в пропиртис

            mailSender.send(email, "Activation code", message);
        }
    }

    public boolean findEmailFromDB(String email) {
        User userFromDb = userRepository.findByEmail(email);

        if (userFromDb == null) {
            return false;
        }

        return true;
    }

    public boolean findUsernameFromDB(String username) {
        User userFromDb = userRepository.findByUsername(username);

        if (userFromDb == null) {
            return false;
        }

        return true;
    }

    public User findIdFromDB(long id) {
        User userFromDb = null;

        try {
            userFromDb = userRepository.findById(id);
        } catch (Exception e) {

        }

        return userFromDb;
    }

    public User findIdFromDB(String id) {
        User userFromDb = null;

        try {
            if (id.equals("") || !id.matches("[0-9]+")) {
                throw new Exception();
            }

            userFromDb = userRepository.findById(Long.parseLong(id));
        } catch (Exception e) {

        }


        return userFromDb;
    }

    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        if (findUsernameFromDB(user.getUsername())) {
            return;
        }

        user.setActive(false);

        // генерируем код UUID
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        userRepository.save(user);

        String message = String.format(
                "Hello, %s \n" +
                        "Welcome to blog. Please , visit next link: http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        sendMessage(user.getNewEmail(), message);
    }


    public String activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return "Activation code is not found";
        }

        if (user.getNewEmail() != null && !isEmailFree(user, user.getNewEmail())) {
            return "Пользователь с такой почтой уже зарегистрирован";
        }

        // Устанавливаем роль
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        user.setRoles(roles);

        // Заменяем пароль и почту новыми, если они есть
        if (user.getNewEmail() != null) {
            user.setEmail(user.getNewEmail());
            user.setNewEmail(null);
        }
        if (user.getNewPassword() != null) {
            user.setPassword(user.getNewPassword());
            user.setNewPassword(null);
        }

        user.setActivationCode(null);
        user.setActive(true);


        userRepository.save(user);


        return "User successfully activated";
    }

    public void userSave(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }


        userRepository.save(user);
    }

    public String updateProfile(User user, String password, String email, String oldPassword) {
        // Проверка старого пароля для сохранения изменений
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            return "Вы ввели неправильный пароль";

        if (!isEmailFree(user, email)) {
            return "Пользователь с такой почтой уже зарегистрирован";
        }

        String oldEmail = user.getEmail();

        boolean isEmailChanged = (!StringUtils.isEmpty(email) &&
                !email.equals(oldEmail));
        boolean isPasswordChanged = !StringUtils.isEmpty(password) &&
                !passwordEncoder.matches(password, user.getPassword());

        boolean userChanged = isEmailChanged || isPasswordChanged;

        if (!userChanged) {
            return "Изменения не добавлены";
        }

        if (isEmailChanged) {
            user.setNewEmail(email);
        }

        if (isPasswordChanged) {
            user.setNewPassword(passwordEncoder.encode(password));
        }


        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        String message = String.format(
                "Hello, %s \n" +
                        "Confirm profile changes. Please , visit next link: http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        if (isEmailChanged) {
            sendMessage(email, message);
        } else {
            sendMessage(oldEmail, message);
        }

        return String.format("Подтвердите изменения на почте %s", user.getEmail());

    }


    public boolean isEmailFree(User user, String email) {
        User userFromBb = userRepository.findByEmail(email);

        if (userFromBb == null)
            return true;

        if (userFromBb.equals(user))
            return true;

        return false;
    }

    public void subscribe(User user, User userProfile) {
        userProfile.getSubscribers().add(user);

        userRepository.save(userProfile);

    }

    public void unsubscribe(User user, User userProfile) {
        userProfile.getSubscribers().remove(user);

        userRepository.save(userProfile);
    }
}
