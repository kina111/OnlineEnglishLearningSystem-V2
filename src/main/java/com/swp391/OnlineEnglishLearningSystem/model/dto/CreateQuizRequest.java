package com.swp391.OnlineEnglishLearningSystem.model.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

public class CreateQuizRequest {
    @NotBlank(message = "Lesson title is required")
    @Size(min = 5, max = 200, message = "Lesson title must be between 5-200 characters")
    private String title;

    @NotNull(message = "Pass score is required")
    @Min(value = 0, message = "Pass score must be positive or zero")
    @Max(value = 100, message = "Pass score must be less than or equal to 100")
    private Integer passRate;

    @NotNull(message = "Thời gian làm bài không được để trống")
    @Positive(message = "Time limit must be a positive number")
    private Integer timeLimitInMinutes;

    @NotNull(message = "Số câu hỏi không được để trống")
    @Min(value = 1, message = "Number of questions must be greater than or equal to 1")
    @Max(value = 100, message = "Number of questions must be less than or equal to 100")
    private Integer numberOfQuestions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
