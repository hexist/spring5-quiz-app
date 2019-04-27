package com.gusev.spring5quizapp.repository;

import com.gusev.spring5quizapp.model.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {

    List<Question> findByQuizId(Long quizId);
}