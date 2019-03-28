package com.gusev.spring5quizapp.repository;

import com.gusev.spring5quizapp.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);
}