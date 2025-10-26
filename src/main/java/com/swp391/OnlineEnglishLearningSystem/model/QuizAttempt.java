package com.swp391.OnlineEnglishLearningSystem.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class QuizAttempt {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Lesson lesson;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime completedTime;
    private Double score;
    private Boolean passed = false;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<QuizAttemptQuestion> questions = new ArrayList<>();

    public QuizAttempt() {
    }
    public QuizAttempt(User user, Lesson lesson, LocalDateTime startTime, LocalDateTime endTime, Double score, Boolean passed, List<QuizAttemptQuestion> questions) {
        this.user = user;
        this.lesson = lesson;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
        this.passed = passed;
        this.questions = questions;
    }

    public QuizAttempt(Long id, User user, Lesson lesson, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime completedTime, Double score, Boolean passed, List<QuizAttemptQuestion> questions) {
        this.id = id;
        this.user = user;
        this.lesson = lesson;
        this.startTime = startTime;
        this.endTime = endTime;
        this.completedTime = completedTime;
        this.score = score;
        this.passed = passed;
        this.questions = questions;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public List<QuizAttemptQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizAttemptQuestion> questions) {
        this.questions = questions;
    }
}

