package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "short_answer_options")
public class ShortAnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Đáp án không được để trống.")
    @Size(max = 255, message = "Đáp án không được vượt quá 255 ký tự.")
    @Column(nullable = false, length = 255)
    private String solutionText; // Nội dung đáp án đúng

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public ShortAnswerOption() {
        super();
    }

    public ShortAnswerOption(String solutionText, Question question) {
        this.solutionText = solutionText;
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSolutionText() {
        return solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
