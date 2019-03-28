package com.gusev.spring5quizapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String text;
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Quiz() {
    }

    public Quiz(String name, String text, String tag, User author) {
        this.name = name;
        this.text = text;
        this.tag = tag;
        this.author = author;
    }

    public String getAuthorName(){
        return author != null ? author.getUsername() : "No author";
    }
}

