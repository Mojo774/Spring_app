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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    public boolean findEmail(User user){
        User userFromDb = userRepository.findByEmail(user.getEmail());

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

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        // генерируем код UUID
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())){
            // Ссылку можно вынести в пропиртис
            String message = String.format(
                    "Hello, %s \n" +
                            "Welcome to blog. Please , visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(),"Activation code", message);
        }



    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null){
            return false;
        }

        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }
}
