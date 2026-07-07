package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "answer_options")
public class AnswerOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(500)")
    @NotBlank(message = "Nội dung lựa chọn không được để trống.")
    @Size(max = 500, message = "Nội dung lựa chọn không được vượt quá 500 ký tự.")
    private String content; // Nội dung lựa chọn (A, B, C, D...)

    @NotNull(message = "Trường 'correct' không được để trống.")
    @Column(nullable = false)
    private Boolean correct; // Đánh dấu đây là đáp án đúng

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String explanation; // Giải thích cho lựa chọn này

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public AnswerOption(String content, Boolean correct, String explanation, Question question) {
        this.content = content;
        this.correct = correct;
        this.explanation = explanation;
        this.question = question;
    }
}
