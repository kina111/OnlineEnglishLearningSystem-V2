package com.swp391.OnlineEnglishLearningSystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteRequest {
    @NotBlank(message = "Chưa có thời gian!")
    private String timeAtLesson;

    @NotBlank(message = "Vui lòng nhập nội dung ghi chú!")
    private String content;
}
