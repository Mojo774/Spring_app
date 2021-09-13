package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/profile/{id}")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String oldPassword,
            Model model
    ) {

        String message = userService.updateProfile(user, password, email, oldPassword);


        model.addAttribute("message", message);
        model.addAttribute("userProfile", user);
        model.addAttribute("user",user);

        return "profile";


    }


    // TODO: заменить id на user в параметрах

    @GetMapping("profile/subscribe/{id}")
    public String subscribe(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, Model model){
        User userProfile = userService.findIdFromDB(id);

        userService.subscribe(user, userProfile);


        return "redirect:/profile/"+id;
    }

    @GetMapping("profile/unsubscribe/{id}")
    public String unsubscribe(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, Model model){
        User userProfile = userService.findIdFromDB(id);


        userService.unsubscribe(user, userProfile);


        return "redirect:/profile/"+id;
    }
}
