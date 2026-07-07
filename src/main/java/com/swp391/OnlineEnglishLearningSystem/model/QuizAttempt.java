package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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

    public QuizAttempt(User user, Lesson lesson, LocalDateTime startTime, LocalDateTime endTime,
                       Double score, Boolean passed, List<QuizAttemptQuestion> questions) {
        this.user = user;
        this.lesson = lesson;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
        this.passed = passed;
        this.questions = questions;
    }

    public QuizAttempt(Long id, User user, Lesson lesson, LocalDateTime startTime, LocalDateTime endTime,
                       LocalDateTime completedTime, Double score, Boolean passed,
                       List<QuizAttemptQuestion> questions) {
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
}
