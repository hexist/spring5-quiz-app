package com.gusev.spring5quizapp.controller;

import com.gusev.spring5quizapp.model.Role;
import com.gusev.spring5quizapp.model.User;
import com.gusev.spring5quizapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String usersList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEdit(@PathVariable User user, Model model) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("user", user);

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam("userId") User user,
            @RequestParam String username,
            @RequestParam Map<String, String> form
    ) {
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("email", user.getEmail());

        return "editProfile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String email,
            @RequestParam String password
    ) {
        userService.updateProfile(user, password, email);

        return "redirect:/user/profile/edit";
    }
}
