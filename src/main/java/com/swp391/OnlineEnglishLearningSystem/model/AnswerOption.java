package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "answer_options")
public class AnswerOption extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(500)")
    @NotBlank(message = "Answer option content is required")
    @Size(max = 500, message = "Answer option content must be between 0-500 characters")
    private String content; // Nội dung lựa chọn (A, B, C, D...)

    @NotNull
    @Column(nullable = false)
    private Boolean correct; // Đánh dấu đây là đáp án đúng

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String explanation; // Giải thích cho lựa chọn này

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public AnswerOption() {
        super();
    }

    public AnswerOption(String content, Boolean correct, String explanation, Question question) {
        this.content = content;
        this.correct = correct;
        this.explanation = explanation;
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
