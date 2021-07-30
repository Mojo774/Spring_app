package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "profile";
    }

}
