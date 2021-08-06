package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    /*@GetMapping("/login")
    public String login(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "registration";
    }

    @PostMapping("/login")
    public String loginUser(@Valid User user, BindingResult bindingResult, Model model){

        model.addAttribute("user",user);

        if (bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            if (userService.findEmail(user)) {
                errorsMap.put("newEmailError","пользователь с такой почтой уже существует");
            }


            if (userService.findUsername(user)) {
                errorsMap.put("usernameError", "пользователь с таким именем уже существует");
            }

            model.mergeAttributes(errorsMap);


            return "registration";

        } else {

            userService.addUser(user);

            return "home";
        }
    }*/

    @GetMapping("/registration")
    public String registration(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model){

        model.addAttribute("user",user);

        if (bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            if (userService.findEmail(user)) {
                errorsMap.put("newEmailError","пользователь с такой почтой уже существует");
            }


            if (userService.findUsername(user)) {
                errorsMap.put("usernameError", "пользователь с таким именем уже существует");
            }

            model.mergeAttributes(errorsMap);


            return "registration";

        } else {


            userService.addUser(user);

            return "redirect:/login";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if (isActivated){
            model.addAttribute("message", "User successfully activated");

        } else {
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }


}
