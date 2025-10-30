package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;

@Entity
public class QuizAttemptSelectedOption {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuizAttemptQuestion attemptQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    private AnswerOption option;

    private String selectedValue; // for fill-in-the-blank

    public QuizAttemptSelectedOption() {
    }
    public QuizAttemptSelectedOption(QuizAttemptQuestion attemptQuestion, AnswerOption option, String selectedValue) {
        this.attemptQuestion = attemptQuestion;
        this.option = option;
        this.selectedValue = selectedValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizAttemptQuestion getAttemptQuestion() {
        return attemptQuestion;
    }

    public void setAttemptQuestion(QuizAttemptQuestion attemptQuestion) {
        this.attemptQuestion = attemptQuestion;
    }

    public AnswerOption getOption() {
        return option;
    }

    public void setOption(AnswerOption option) {
        this.option = option;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
}

