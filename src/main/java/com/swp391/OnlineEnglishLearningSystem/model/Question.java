package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question extends BaseEntity {
    public enum QuestionType {
        MULTIPLE_CHOICE("Trắc nghiệm nhiều lựa chọn"),
        SHORT_ANSWER("Điền từ/câu trả lời ngắn");

        private final String displayName;

        QuestionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum MediaType {
        NONE,
        IMAGE,
        AUDIO,
        VIDEO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    @NotBlank(message = "Nội dung câu hỏi không được để trống.")
    private String content; // Nội dung text của câu hỏi

    @Column(nullable = false)
    @NotNull(message = "Loại câu hỏi không được để trống.")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType; // Loại câu hỏi (ví dụ: trắc nghiệm, tự luận...)

    // --- Phần dành cho Media ---
    @NotNull(message = "Loại phương tiện (media) không được để trống, ví dụ: NONE.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType; // Loại media đính kèm (nếu có)

    @Size(max = 2048, message = "Đường dẫn media không được vượt quá 2048 ký tự.")
    @Column(length = 2048, columnDefinition = "NVARCHAR(MAX)") // Giới hạn độ dài trong CSDL
    private String mediaUrl; // Đường dẫn tới file media

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOption> answerOptions = new ArrayList<>();

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private ShortAnswerOption shortAnswerOption;

    public Question(String content, QuestionType questionType, MediaType mediaType, String mediaUrl, Lesson lesson) {
        this.content = content;
        this.questionType = questionType;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.lesson = lesson;
    }
}
