package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "answer_options")
public class AnswerOption {
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
}
