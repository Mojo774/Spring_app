package com.example.demo.controllers;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

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

    @PostMapping
    public String userSave(
            @RequestParam Map<String,String> form,
            @RequestParam("userId") User user){

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
        return "redirect:/user";
    }

}
