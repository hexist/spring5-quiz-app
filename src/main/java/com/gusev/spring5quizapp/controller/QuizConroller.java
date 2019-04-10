package com.gusev.spring5quizapp.controller;

import com.gusev.spring5quizapp.model.Quiz;
import com.gusev.spring5quizapp.model.User;
import com.gusev.spring5quizapp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Controller
public class QuizConroller {
    @Autowired
    private QuizRepository quizRepository;

    @GetMapping("/quizAdd")
    public String quizAdd(
            Model model) {
        model.addAttribute("quiz", new Quiz());

        return "quizAdd";
    }

    @GetMapping("/quizEdit")
    public String quizEdit(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Quiz quiz,
            Model model) {
        if (!currentUser.equals(quiz.getAuthor())) {
            return "redirect:/quizAdd";
        }

        model.addAttribute("quiz", quiz);

        return "quizEdit";
    }

    @PostMapping("/quizSave")
    public String quizSave(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute Quiz quiz,
            BindingResult bindingResult,
            Model model
    ) {
        quiz.setAuthor(user);

        if (bindingResult.hasErrors()) {
            return "quizEdit";
        }

        quizRepository.save(quiz);

        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.addAttribute("quizzes", quizzes);

        return "redirect:/index";
    }

    @GetMapping("/user-quizzes/{user}")
    public String userQuizzes(
            @PathVariable User user,
            Model model
    ) {
        if (user != null) {
            Set<Quiz> quizzes = user.getQuizzes();
            model.addAttribute("quizzes", quizzes);
        }
        model.addAttribute("user", user);

        return "userQuizzes";
    }
}
