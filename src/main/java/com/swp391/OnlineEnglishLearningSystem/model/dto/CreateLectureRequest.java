package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class CreateLectureRequest {
    @NotBlank(message = "Vui lòng nhập tên bài học")
    @Size(min = 5, max = 200, message = "Tên bài học trong khoảng 5-200 kí tự")
    private String title;

    // --- Lecture fields ---
    @NotNull(message = "Vui lòng nhập thời lượng ước tính")
    @Positive(message = "Vui lòng nhập thời lượng ước tính hợp lệ")
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
