package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class CreateLectureRequest {
    @NotBlank(message = "Lesson title is required")
    @Size(min = 5, max = 200, message = "Lesson title must be between 5-200 characters")
    private String title;

    // --- Lecture fields ---
    @NotNull(message = "Lesson type is required")
    @Positive(message = "Lesson estimated time must be a positive number")
    private Integer estimatedTime;

    @NotBlank(message = "Nội dung bài giảng không được để trống")
    private String htmlContent;

    private MultipartFile video;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public MultipartFile getVideo() {
        return video;
    }

    public void setVideo(MultipartFile video) {
        this.video = video;
    }
}
