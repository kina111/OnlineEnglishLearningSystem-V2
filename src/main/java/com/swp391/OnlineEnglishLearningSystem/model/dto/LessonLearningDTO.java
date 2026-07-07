package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonLearningDTO {
    private Long id;
    private Lesson.LessonType lessonType;
    private String title;
    private String videoUrl;
    private String htmlContent;
    private LocalDateTime lastUpdatedDate;

    public LessonLearningDTO(Lesson lesson) {
        this.id = lesson.getId();
        this.lessonType = lesson.getLessonType();
        this.title = lesson.getTitle();
        this.videoUrl = lesson.getVideoUrl();
        this.htmlContent = lesson.getHtmlContent();
        this.lastUpdatedDate = lesson.getUpdatedAt();
    }
}
