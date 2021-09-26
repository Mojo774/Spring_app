package com.example.demo.service;

import com.example.demo.TestEnabled;
import com.example.demo.TestEnabledPrefix;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;



@SpringBootTest
@TestEnabledPrefix(prefix = "app.UserService.test")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean(name = "getPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @TestEnabled(property = "addUser")
    @Test
    void addUser() {
        User user = new User();

        user.setNewEmail("some@mail.ru");

        boolean isUserCreated = userService.addUser(user);

        Assertions.assertTrue(isUserCreated);
        Assertions.assertNotNull(user.getActivationCode());
        Assertions.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        // spy object
        // 1 раз был вызван метод save у userRepository и ему был передан user как аргумент
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        // то же самое
        // где 1 аргумент должен быть равен user.getEmail()
        // а остальные два могут быть любой строкой
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getNewEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void addUserFailTest() {
        User user = new User();

        user.setUsername("John");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("John");

        boolean isUserCreated = userService.addUser(user);

        Assertions.assertFalse(isUserCreated);

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void activateUser() {
        User user = new User();

        user.setActivationCode("bingo!");

        Mockito.doReturn(user)
                .when(userRepository)
                .findByActivationCode("activate");

        String isUserActivated = userService.activateUser("activate");


        Assertions.assertEquals("User successfully activated",isUserActivated);
        Assertions.assertNull(user.getActivationCode());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        String isUserActivated = userService.activateUser("activate me");

        Assertions.assertNotEquals("User successfully activated",isUserActivated);

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

}