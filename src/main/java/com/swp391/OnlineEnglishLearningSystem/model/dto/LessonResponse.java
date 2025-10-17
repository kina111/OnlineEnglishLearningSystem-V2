package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;

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

    public Lesson.LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(Lesson.LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
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

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
