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

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/index")
    public String index(Map<String, Object> model) {
        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.put("quiz", new Quiz());
        model.put("quizzes", quizzes);

        return "index";
    }

    @PostMapping("/index")
    public String add(@ModelAttribute Quiz quiz, Map<String, Object> model) {
        Quiz newQuiz = new Quiz(quiz.getName(), quiz.getText(), quiz.getTag());

        quizRepository.save(newQuiz);

        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.put("quizzes", quizzes);

        return "index";
    }

    @PostMapping(value = "index/filter")
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

    @PostMapping("index/delete")
    public String delete(@RequestParam Integer id, Map<String, Object> model) {
        quizRepository.deleteById(id);
        Iterable<Quiz> quizzes = quizRepository.findAll();

        model.put("quiz", new Quiz());
        model.put("quizzes", quizzes);

        return "index";
    }
}