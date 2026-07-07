package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonResponse {
    private Long id;
    private String title;
    private Integer orderNumber;
    private Lesson.LessonType lessonType;
    private Integer estimatedTime;
    private String videoUrl;
    private String htmlContent;
    private Integer passRate;
    private Integer timeLimitInMinutes;
    private Integer numberOfQuestions;

    public LessonResponse(Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.orderNumber = lesson.getOrderNumber();
        this.lessonType = lesson.getLessonType();
        this.estimatedTime = lesson.getEstimatedTime();
        this.videoUrl = lesson.getVideoUrl();
        this.htmlContent = lesson.getHtmlContent();
        this.passRate = lesson.getPassRate();
        this.timeLimitInMinutes = lesson.getTimeLimitInMinutes();
        this.numberOfQuestions = lesson.getNumberOfQuestions();
    }
}
