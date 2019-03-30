package com.gusev.spring5quizapp.controller;

import com.gusev.spring5quizapp.model.Quiz;
import com.gusev.spring5quizapp.model.User;
import com.gusev.spring5quizapp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class MainController {
    @Autowired
    private QuizRepository quizRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/index")
    public String index(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model
    ) {
        Iterable<Quiz> quizzes;

        if (filter != null && !filter.isEmpty()) {
            quizzes = quizRepository.findByTag(filter);
        } else {
            quizzes = quizRepository.findAll();
        }

        model.addAttribute("isAdmin", user.isAdmin());
        model.addAttribute("quizzes", quizzes);

        return "index";
    }

    @GetMapping("/quizAdd")
    public String index(Model model) {
        model.addAttribute("quiz", new Quiz());

        return "quizAdd";
    }

    @PostMapping("/quizAdd")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute Quiz quiz,
            BindingResult bindingResult,
            Model model
    ) {
        quiz.setAuthor(user);

        if (bindingResult.hasErrors()) {
            return "quizAdd";
        }

        quizRepository.save(quiz);

        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.addAttribute("quizzes", quizzes);

        return "redirect:/index";
    }

    @PostMapping("index/delete")
    public String delete(@RequestParam Integer id, Model model) {
        quizRepository.deleteById(id);
        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.addAttribute("quiz", new Quiz());
        model.addAttribute("quizzes", quizzes);

        return "redirect:/index";
    }
}