package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;

import java.time.LocalDateTime;

public class LessonLearningDTO {
    private Long id;
    private Lesson.LessonType lessonType;
    private String title;
    private String videoUrl;
    private String htmlContent;
    private LocalDateTime lastUpdatedDate;

    public LessonLearningDTO(Lesson lesson){
        this.id = lesson.getId();
        this.lessonType = lesson.getLessonType();
        this.title = lesson.getTitle();
        this.videoUrl = lesson.getVideoUrl();
        this.htmlContent = lesson.getHtmlContent();
        this.lastUpdatedDate = lesson.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson.LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(Lesson.LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
