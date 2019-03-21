package com.gusev.spring5quizapp.controller;

import com.gusev.spring5quizapp.model.Quiz;
import com.gusev.spring5quizapp.model.User;
import com.gusev.spring5quizapp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private QuizRepository quizRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/index")
    public String index(@RequestParam(required = false, defaultValue = "") String filter,  Model model) {
        Iterable<Quiz> quizzes;

        if (filter != null && !filter.isEmpty()) {
            quizzes = quizRepository.findByTag(filter);
        } else {
            quizzes = quizRepository.findAll();
        }

        model.addAttribute("quiz", new Quiz());
        model.addAttribute("quizzes", quizzes);

        return "index";
    }

    @PostMapping("/index")
    public String add(
            @AuthenticationPrincipal User user,
            @ModelAttribute Quiz quiz,
            Model model
    ) {
        Quiz newQuiz = new Quiz(quiz.getName(), quiz.getText(), quiz.getTag(), user);

        quizRepository.save(newQuiz);

        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.addAttribute("quizzes", quizzes);

        return "index";
    }

    @PostMapping("index/delete")
    public String delete(@RequestParam Integer id, Model model) {
        quizRepository.deleteById(id);
        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.addAttribute("quiz", new Quiz());
        model.addAttribute("quizzes", quizzes);

        return "index";
    }
}