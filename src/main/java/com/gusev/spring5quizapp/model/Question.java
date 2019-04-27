package com.gusev.spring5quizapp.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Length(max = 5000, message = "Too long description!")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ElementCollection
    @CollectionTable(name = "option")
    @MapKeyColumn(name = "number")
    @Column(name = "description")
    private Map<Integer, String> options = new HashMap<>();

    private Integer answerNumber;
}
