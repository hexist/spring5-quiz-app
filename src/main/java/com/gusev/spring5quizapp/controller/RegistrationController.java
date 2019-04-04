package com.gusev.spring5quizapp.controller;

import com.gusev.spring5quizapp.model.User;
import com.gusev.spring5quizapp.model.dto.CaptchaResponseDto;
import com.gusev.spring5quizapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    String secret;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());

        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("isRegisterForm", true);
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("passwordConfirmation") String passwordConfirmation,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            Model model
    ) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(CAPTCHA_URL)
                .queryParam("secret", secret)
                .queryParam("response", captchaResponse);

        CaptchaResponseDto response = restTemplate.postForObject(uriBuilder.toUriString(), Collections.emptyList(), CaptchaResponseDto.class);

        if(!response.isSuccess()){
            model.addAttribute("captchaError", "Please, fill captcha!");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)) {
            bindingResult.rejectValue("password", "error.passwordConfirmation", "Passwords don't match!");
        }

        if (bindingResult.hasErrors() || !response.isSuccess()) {
            return "registration";
        }

        if (!userService.addUser(user)) {
            bindingResult.rejectValue("username", "error.usernameExists", "User already exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Your account was successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Wrong activation code");
        }

        model.addAttribute("user", new User());

        return "login";
    }
}
