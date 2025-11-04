package com.swp391.OnlineEnglishLearningSystem.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class FeedbackDTO {
    private Long id;
    private String userName;
    private String userAvatarUrl;

    @NotNull
    @Min(value = 1, message = "Vui lòng đánh giá từ 1-5 sao.")
    @Max(value = 5, message = "Vui lòng đánh giá từ 1-5 sao.")
    private Integer rating;

    @NotNull(message = "Vui lòng nhập nội dung đánh giá.")
    private String review;

    private int helpfulCount;

    private LocalDateTime createdAt;

    public FeedbackDTO() {
        super();
    }

    public FeedbackDTO(Long id, String userName, String userAvatarUrl, Integer rating, String review, int helpfulCount, LocalDateTime createdAt) {
        this.userName = userName;
        this.userAvatarUrl = userAvatarUrl;
        this.rating = rating;
        this.review = review;
        this.helpfulCount = helpfulCount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

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

    public int getHelpfulCount() {
        return helpfulCount;
    }

    public void setHelpfulCount(int helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
