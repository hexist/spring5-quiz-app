package com.gusev.spring5quizapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String text;
    private String tag;

    public Quiz() {
    }

    public Quiz(String name, String text, String tag) {
        this.name = name;
        this.text = text;
        this.tag = tag;
    }
}

