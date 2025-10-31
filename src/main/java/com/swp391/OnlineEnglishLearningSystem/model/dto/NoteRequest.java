package com.swp391.OnlineEnglishLearningSystem.model.dto;

import jakarta.validation.constraints.NotBlank;

public class NoteRequest {
    @NotBlank(message = "Chưa có thời gian!")
    private String timeAtLesson;

    @NotBlank(message = "Vui lòng nhập nội dung ghi chú!")
    private String content;

    public String getTimeAtLesson() {
        return timeAtLesson;
    }

    public void setTimeAtLesson(String timeAtLesson) {
        this.timeAtLesson = timeAtLesson;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
