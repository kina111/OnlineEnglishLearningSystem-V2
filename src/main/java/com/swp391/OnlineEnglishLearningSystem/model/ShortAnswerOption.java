package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "short_answer_options")
public class ShortAnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Đáp án không được để trống.")
    @Size(max = 255, message = "Đáp án không được vượt quá 255 ký tự.")
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String solutionText; // Nội dung đáp án đúng

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public ShortAnswerOption(String solutionText, Question question) {
        this.solutionText = solutionText;
        this.question = question;
    }
}
