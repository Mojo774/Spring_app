package com.example.demo.controllers;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        List<User> list = new ArrayList<>();
        list.add(user);

        model.addAttribute("user", list);

        Map<Role,Boolean> userRoles = new HashMap<>();

        for (Role role: Role.values()){
            if (user.getRoles().contains(role)){
                userRoles.put(role,true);
            } else {
                userRoles.put(role,false);
            }
        }

        model.addAttribute("userRoles", userRoles);
        return "user-edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam Map<String,String> form,
            @RequestParam("userId") User user){

        userService.userSave(user,form);
        return "redirect:/user";
    }

}
