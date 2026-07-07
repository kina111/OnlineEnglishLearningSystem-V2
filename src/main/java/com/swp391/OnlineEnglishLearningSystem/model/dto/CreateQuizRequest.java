package com.swp391.OnlineEnglishLearningSystem.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
public class CreateQuizRequest {
    @NotBlank(message = "Vui lòng nhập tên bài học")
    @Size(min = 5, max = 200, message = "Tên bài học trong khoảng từ 5-200 kí tự")
    private String title;

    @NotNull(message = "Vui lòng nhập phần trăm để qua")
    @Min(value = 0, message = "Phần trăm qua phải trong khoảng 0-100")
    @Max(value = 100, message = "Phần trăm qua phải trong khoảng 0-100")
    private Integer passRate;

    @NotNull(message = "Thời gian làm bài không được để trống")
    @Positive(message = "Vui lòng nhập thời gian làm bài hợp lệ")
    private Integer timeLimitInMinutes;

    @NotNull(message = "Số câu hỏi không được để trống")
    @Min(value = 1, message = "Số câu hỏi trong khoảng 1-100")
    @Max(value = 100, message = "Số câu hỏi trong khoảng 1-100")
    private Integer numberOfQuestions;
}
