package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question extends BaseEntity{
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
    @Column(length = 2048, columnDefinition = "NVARCHAR(255)") // Giới hạn độ dài trong CSDL
    private String mediaUrl; // Đường dẫn tới file media

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOption> answerOptions = new ArrayList<>();

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private ShortAnswerOption shortAnswerOption;

    public Question() {
        super();
    }
    public Question(String content, QuestionType questionType, MediaType mediaType, String mediaUrl, Lesson lesson) {
        this.content = content;
        this.questionType = questionType;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.lesson = lesson;
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

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public ShortAnswerOption getShortAnswerOption() {
        return shortAnswerOption;
    }

    public void setShortAnswerOption(ShortAnswerOption shortAnswerOption) {
        this.shortAnswerOption = shortAnswerOption;
    }
}
