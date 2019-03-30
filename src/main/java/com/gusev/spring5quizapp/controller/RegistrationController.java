package com.gusev.spring5quizapp.controller;

import com.gusev.spring5quizapp.model.User;
import com.gusev.spring5quizapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

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
            @Valid @ModelAttribute User user,
            BindingResult bindingResult
    ) {
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)) {
            bindingResult.rejectValue("password", "error.passwordConfirmation", "Passwords don't match!");
        }
        if (bindingResult.hasErrors()) {
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
