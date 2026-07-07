package com.swp391.OnlineEnglishLearningSystem.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EnrollmentInfoDTO {
    private Long id;
    private String courseTitle;
    private String courseThumbnailUrl;
    private String courseShortDescription;

    private LocalDateTime completedAt;
    private LocalDateTime lastAccessAt;
    private LocalDateTime enrolledAt;

    private int progress;
    private long totalLessons;
    private long completedLessons; //đổi thành long vì COUNT trong sql trả về dữ liệu kiểu long

    public EnrollmentInfoDTO(Long id, String courseTitle, String courseThumbnailUrl,
                              String courseShortDescription, LocalDateTime completedAt,
                              LocalDateTime lastAccessAt, LocalDateTime enrolledAt,
                              long totalLessons, long completedLessons) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.courseThumbnailUrl = courseThumbnailUrl;
        this.courseShortDescription = courseShortDescription;
        this.completedAt = completedAt;
        this.lastAccessAt = lastAccessAt;
        this.enrolledAt = enrolledAt;
        this.totalLessons = totalLessons;
        this.completedLessons = completedLessons;
    }
}
