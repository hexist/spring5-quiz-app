package com.gusev.spring5quizapp.repository;

import com.gusev.spring5quizapp.model.Quiz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizRepository extends CrudRepository<Quiz, Long> {

    List<Quiz> findByTag(String tag);

}
