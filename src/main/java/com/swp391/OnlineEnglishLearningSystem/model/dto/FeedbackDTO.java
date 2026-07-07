package com.swp391.OnlineEnglishLearningSystem.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    public FeedbackDTO(Long id, String userName, String userAvatarUrl, Integer rating,
                       String review, int helpfulCount, LocalDateTime createdAt) {
        this.userName = userName;
        this.userAvatarUrl = userAvatarUrl;
        this.rating = rating;
        this.review = review;
        this.helpfulCount = helpfulCount;
        this.createdAt = createdAt;
    }
}
