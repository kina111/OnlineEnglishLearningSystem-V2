package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "questions")
public class Question {
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
    @NotBlank(message = "Question content is required")
    private String content; // Nội dung text của câu hỏi

    @Column(nullable = false)
    @NotNull(message = "Question order number is required")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType; // **Cột quyết định loại câu hỏi**

    // --- Phần dành cho Media ---
    @NotNull(message = "Media type must be specified (e.g., NONE)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType; // Loại media đính kèm (nếu có)

    @Size(max = 2048, message = "Media URL is too long")
    @URL(message = "Media URL must be a valid URL") // Kiểm tra định dạng URL
    @Column(length = 2048) // Giới hạn độ dài trong CSDL
    private String mediaUrl; // Đường dẫn tới file media

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
}
