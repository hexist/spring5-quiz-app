package com.gusev.spring5quizapp.controller;

import com.gusev.spring5quizapp.model.Question;
import com.gusev.spring5quizapp.model.Quiz;
import com.gusev.spring5quizapp.model.User;
import com.gusev.spring5quizapp.repository.QuestionRepository;
import com.gusev.spring5quizapp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class QuizConroller {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/quizAdd")
    public String quizAdd(
            Model model) {
        model.addAttribute("quiz", new Quiz());
        model.addAttribute("questions", new HashSet<Question>());

        return "quizAdd";
    }

    @GetMapping("/quizDetails/{quiz}")
    public String quizDetails(
            @PathVariable Quiz quiz,
            Model model) {
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        model.addAttribute("question", new Question());
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);

        return "quizDetails";
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


    @PostMapping("/questionAdd")
    public String questionAdd(
            @RequestParam Quiz quiz,
            @ModelAttribute Question question,
            Model model
    ) {
        question.setQuiz(quiz);

        questionRepository.save(question);

        List<Question> questions = questionRepository.findByQuizId(quiz.getId());

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);

        return "redirect:/quizDetails/" + quiz.getId();
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
