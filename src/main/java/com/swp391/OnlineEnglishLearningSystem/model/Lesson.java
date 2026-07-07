package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lessons")
public class Lesson extends BaseEntity {
    public enum LessonType {
        LECTURE("Bài giảng"),
        QUIZ("Bài kiểm tra");

        private final String displayName;

        LessonType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200, columnDefinition = "NVARCHAR(200)")
    @NotBlank(message = "Tiêu đề bài học không được để trống.")
    @Size(min = 5, max = 200, message = "Tiêu đề bài học phải có độ dài từ 5 đến 200 ký tự.")
    private String title;

    @Column(nullable = false)
    @NotNull(message = "Thứ tự bài học không được để trống.")
    @PositiveOrZero(message = "Thứ tự bài học phải là số không âm.")
    private Integer orderNumber;

    @Column(nullable = false)
    @NotNull(message = "Loại bài học không được để trống.")
    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @Positive(message = "Thời lượng ước tính của bài học phải là số dương.")
    private Integer estimatedTime;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String htmlContent;

    // --- Dành cho bài giảng ---
    private String videoUrl;
    private Long duration = 0L; // tính bằng milliseconds

    // --- Dành cho bài kiểm tra ---
    @Column(name = "pass_rate")
    @Min(value = 0, message = "Điểm đạt yêu cầu phải lớn hơn hoặc bằng 0.")
    @Max(value = 100, message = "Điểm đạt yêu cầu phải nhỏ hơn hoặc bằng 100.")
    private Integer passRate;

    @Positive(message = "Giới hạn thời gian phải là số dương.")
    private Integer timeLimitInMinutes;

    @Column(name = "number_of_questions")
    @Min(value = 1, message = "Số lượng câu hỏi phải lớn hơn hoặc bằng 1.")
    @Max(value = 100, message = "Số lượng câu hỏi phải nhỏ hơn hoặc bằng 100.")
    private Integer numberOfQuestions;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    public Lesson() {
        super();
    }

    public Lesson(String title, Integer orderNumber, LessonType lessonType, Integer estimatedTime,
                  Integer passRate, Integer timeLimitInMinutes, Integer numberOfQuestions) {
        this.title = title;
        this.orderNumber = orderNumber;
        this.lessonType = lessonType;
        this.estimatedTime = estimatedTime;
        this.passRate = passRate;
        this.timeLimitInMinutes = timeLimitInMinutes;
        this.numberOfQuestions = numberOfQuestions;
    }

    public Lesson(String title, Integer orderNumber, LessonType lessonType, Integer estimatedTime,
                  Integer passRate, Integer timeLimitInMinutes, List<Question> questions, Chapter chapter) {
        this.title = title;
        this.orderNumber = orderNumber;
        this.lessonType = lessonType;
        this.estimatedTime = estimatedTime;
        this.passRate = passRate;
        this.timeLimitInMinutes = timeLimitInMinutes;
        this.questions = questions;
        this.chapter = chapter;
    }
}
