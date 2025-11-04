package com.swp391.OnlineEnglishLearningSystem.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FeedbackRequest {
    @NotNull
    @Min(value = 1, message = "Vui lòng đánh giá từ 1-5 sao.")
    @Max(value = 5, message = "Vui lòng đánh giá từ 1-5 sao.")
    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    @NotNull(message = "Vui lòng nhập nội dung đánh giá.")
    private String review;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
