package com.example.demo.service;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);


        if(user == null) {
            throw new UsernameNotFoundException(username);
        }



        return user;

    }

    private void sendMessage(String email, String message) {
        if (!StringUtils.isEmpty(email)){
            // Ссылку можно вынести в пропиртис

            mailSender.send(email,"Activation code", message);
        }
    }

    public boolean findEmail(User user){
        User userFromDb = userRepository.findByEmail(user.getNewEmail());

        if (userFromDb == null){
            return false;
        }

        return true;
    }

    public boolean findUsername(User user){
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb == null){
            return false;
        }

        return true;
    }

    public void addUser(User user){
        if (findUsername(user)){
            return;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        // генерируем код UUID
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        String message = String.format(
                "Hello, %s \n" +
                        "Welcome to blog. Please , visit next link: http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        sendMessage(user.getNewEmail(), message);
    }


    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null){
            return false;
        }

        // Заменяем пароль и почту новыми, если они есть
        if (user.getNewEmail() != null){
            user.setEmail(user.getNewEmail());
            user.setNewEmail(null);
        }
        if (user.getNewPassword() != null){
            user.setPassword(user.getNewPassword());
            user.setNewPassword(null);
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void userSave(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key: form.keySet()){
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        System.out.println(form.toString());
        userRepository.save(user);
    }

    public boolean updateProfile(User user, String password, String email, String oldPassword) {
        if (!oldPassword.equals(user.getPassword()))
            return false;

        String oldEmail = user.getEmail();
        boolean isEmailChanged = (!StringUtils.isEmpty(email) && !email.equals(oldEmail));
        boolean isPasswordChanged = !StringUtils.isEmpty(password);

        boolean userChanged = isEmailChanged || isPasswordChanged;

        if (isEmailChanged){
            user.setNewEmail(email);
        }

        if (isPasswordChanged){
            user.setNewPassword(password);
        }

        if (userChanged) {

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

        }
        return true;
    }


}
