package com.example.demo.controllers;

import com.example.demo.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String home(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("username",user.getUsername());
        return "profile";
    }

}
