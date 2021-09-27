package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.models.dto.CaptchaResponseDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model,
            String passwordCheck){

        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.
                postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        model.addAttribute("user",user);

        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
        // Дополнительные проверки полей
        // Возможные ошибки в полях при регистрации
        if (userService.findEmailFromDB(user.getNewEmail()) != null) {
            errorsMap.put("newEmailError","пользователь с такой почтой уже существует");
        }
        if (StringUtils.isEmpty(user.getNewEmail())) {
            errorsMap.put("newEmailError","Email cannot be empty");
        }
        if (userService.findUsernameFromDB(user.getUsername()) != null) {
            errorsMap.put("usernameError", "пользователь с таким именем уже существует");
        }
        if (!user.getPassword().equals(passwordCheck)){
            errorsMap.put("passwordCheckError", "пароли не совпадают");
        }
        if (!response.isSuccess()){
            errorsMap.put("captchaError","Fill captcha");
        }

        if (!errorsMap.isEmpty() || !response.isSuccess()){

            model.mergeAttributes(errorsMap);

            return "registration";
        } else {

            userService.addUser(user);

            return "redirect:/login";
        }
    }



    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        String message = userService.activateUser(code);

        model.addAttribute("message", message);

        return "login";
    }


}
