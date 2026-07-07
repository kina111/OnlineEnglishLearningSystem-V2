package com.swp391.OnlineEnglishLearningSystem.model;

import com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "feedbacks")
public class Feedback extends BaseEntity {

    public enum FeedbackStatus {
        PENDING,    // Chờ duyệt
        APPROVED,   // Đã duyệt
        REJECTED    // Bị từ chối
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1, message = "Vui lòng đánh giá từ 1-5 sao.")
    @Max(value = 5, message = "Vui lòng đánh giá từ 1-5 sao.")
    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    @NotNull(message = "Vui lòng nhập nội dung đánh giá.")
    private String review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackStatus status; // Để kiểm duyệt

    @Column(name = "helpful_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int helpfulCount = 0;

    @Column(name = "not_helpful_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int notHelpfulCount = 0;

    /*
     * optional = false: Một feedback BẮT BUỘC phải gắn với 1 enrollment.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;

    // Giữ no-args constructor: có logic init status = APPROVED
    public Feedback() {
        this.status = FeedbackStatus.APPROVED; // Mặc định là duyệt
    }

    public Feedback(FeedbackRequest feedbackRequest, Enrollment enrollment) {
        this.rating = feedbackRequest.getRating();
        this.review = feedbackRequest.getReview();
        this.enrollment = enrollment;
        this.status = FeedbackStatus.APPROVED;
    }

    public Feedback(Integer rating, String review, Enrollment enrollment) {
        this.rating = rating;
        this.review = review;
        this.enrollment = enrollment;
        this.status = FeedbackStatus.APPROVED;
    }

    // (Đừng quên thêm equals() và hashCode() dựa trên ID!)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return id != null && id.equals(feedback.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }
}
