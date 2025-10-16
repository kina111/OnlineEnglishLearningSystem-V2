package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
public class Lesson extends BaseEntity{
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
    @NotBlank(message = "Lesson title is required")
    @Size(min = 5, max = 200, message = "Lesson title must be between 5-200 characters")
    private String title;

    @Column(nullable = false)
    @PositiveOrZero(message = "Lesson order number must be positive or zero")
    @NotNull(message = "Lesson order number is required")
    private Integer orderNumber;

    @Column(nullable = false)
    @NotNull(message = "Lesson type is required")
    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @Positive(message = "Lesson estimated time must be a positive number")
    private Integer estimatedTime;

    private String htmlContent;
    //--- danh cho Lecture ---
    private String videoUrl;


    //---dành cho Quiz---
    @Column(nullable = false, name = "pass_rate")
    @Min(value = 0, message = "Pass score must be positive or zero")
    @Max(value = 100, message = "Pass score must be less than or equal to 100")
    private Integer passRate;

    @Positive(message = "Time limit must be a positive number")
    private Integer timeLimitInMinutes;

    @Column(nullable = false, name = "number_of_questions")
    @Min(value = 1, message = "Number of questions must be greater than or equal to 1")
    @Max(value = 100, message = "Number of questions must be less than or equal to 100")
    private Integer numberOfQuestions;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    public Lesson() {
        super();
    }

    public Lesson(String title, Integer orderNumber, LessonType lessonType, Integer estimatedTime, Integer passRate, Integer timeLimitInMinutes, Integer numberOfQuestions) {
        this.title = title;
        this.orderNumber = orderNumber;
        this.lessonType = lessonType;
        this.estimatedTime = estimatedTime;
        this.passRate = passRate;
        this.timeLimitInMinutes = timeLimitInMinutes;
        this.numberOfQuestions = numberOfQuestions;
    }

    public Lesson(String title, Integer orderNumber, LessonType lessonType, Integer estimatedTime, Integer passRate, Integer timeLimitInMinutes, List<Question> questions, Chapter chapter) {
        this.title = title;
        this.orderNumber = orderNumber;
        this.lessonType = lessonType;
        this.estimatedTime = estimatedTime;
        this.passRate = passRate;
        this.timeLimitInMinutes = timeLimitInMinutes;
        this.questions = questions;
        this.chapter = chapter;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getPassRate() {
        return passRate;
    }

    public void setPassRate(Integer passRate) {
        this.passRate = passRate;
    }

    public Integer getTimeLimitInMinutes() {
        return timeLimitInMinutes;
    }

    public void setTimeLimitInMinutes(Integer timeLimitInMinutes) {
        this.timeLimitInMinutes = timeLimitInMinutes;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
