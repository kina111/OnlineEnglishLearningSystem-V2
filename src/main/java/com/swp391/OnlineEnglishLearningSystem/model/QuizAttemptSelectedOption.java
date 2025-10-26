package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;

@Entity
public class QuizAttemptSelectedOption {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne private QuizAttemptQuestion attemptQuestion;
    @ManyToOne private AnswerOption option;

    private String selectedValue; // for fill-in-the-blank
}

