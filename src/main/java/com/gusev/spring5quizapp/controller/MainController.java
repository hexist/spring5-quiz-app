package com.gusev.spring5quizapp.controller;

import com.gusev.spring5quizapp.model.Quiz;
import com.gusev.spring5quizapp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private QuizRepository quizRepository;

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "Alex") String name,
            Model model
    ) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping
    public String index(Map<String, Object> model) {
        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.put("quiz", new Quiz());
        model.put("quizzes", quizzes);

        return "index";
    }

    @PostMapping
    public String add(@ModelAttribute Quiz quiz, Map<String, Object> model) {
        Quiz newQuiz = new Quiz(quiz.getName(), quiz.getText(), quiz.getTag());

        quizRepository.save(newQuiz);

        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.put("quizzes", quizzes);

        return "index";
    }

    @PostMapping(value = "filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Quiz> quizzes;

        if (filter != null && !filter.isEmpty()) {
            quizzes = quizRepository.findByTag(filter);
        } else {
            quizzes = quizRepository.findAll();
        }

        model.put("quiz", new Quiz());
        model.put("quizzes", quizzes);

        return "index";
    }

    @PostMapping("delete")
    public String delete(@RequestParam Integer id, Map<String, Object> model) {
        quizRepository.deleteById(id);
        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.put("quiz", new Quiz());
        model.put("quizzes", quizzes);

        return "index";
    }
}