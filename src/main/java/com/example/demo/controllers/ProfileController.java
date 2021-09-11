package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    // переадресация на /{id} тк я не знаю как добавить id в header
    // там только на /profile можно ссылку сделать
    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model) {
        return "redirect:/profile/"+user.getId();
    }

    @GetMapping("/profile/{id}")
    public String getProfile2(@PathVariable(value = "id") long id, @AuthenticationPrincipal User user, Model model) {

        User userProfile = userService.findIdFromDB(id);

        if (userProfile == null) {
            return "redirect:/";
        }


        model.addAttribute("userProfile", userProfile);
        model.addAttribute("user",user);


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
