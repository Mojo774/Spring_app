package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        List<User> subscribers = new ArrayList<>(user.getSubscribers());
        List<User> subscriptions = new ArrayList<>(user.getSubscriptions());


        System.out.println(subscribers);
        System.out.println(subscriptions);

        model.addAttribute("subscribers", subscribers);
        model.addAttribute("subscriptions", subscriptions);

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String oldPassword,
            Model model
    ) {


        String message = userService.updateProfile(user, password, email, oldPassword);

        model.addAttribute("message", message);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";


    }
}
