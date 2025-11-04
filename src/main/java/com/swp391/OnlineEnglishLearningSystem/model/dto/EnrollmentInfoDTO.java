package com.swp391.OnlineEnglishLearningSystem.model.dto;

import java.time.LocalDateTime;

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

    public EnrollmentInfoDTO(Long id, String courseTitle, String courseThumbnailUrl, String courseShortDescription, LocalDateTime completedAt, LocalDateTime lastAccessAt, LocalDateTime enrolledAt, long totalLessons, long completedLessons) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseThumbnailUrl() {
        return courseThumbnailUrl;
    }

    public void setCourseThumbnailUrl(String courseThumbnailUrl) {
        this.courseThumbnailUrl = courseThumbnailUrl;
    }

    public String getCourseShortDescription() {
        return courseShortDescription;
    }

    public void setCourseShortDescription(String courseShortDescription) {
        this.courseShortDescription = courseShortDescription;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getLastAccessAt() {
        return lastAccessAt;
    }

    public void setLastAccessAt(LocalDateTime lastAccessAt) {
        this.lastAccessAt = lastAccessAt;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(long totalLessons) {
        this.totalLessons = totalLessons;
    }

    public long getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(long completedLessons) {
        this.completedLessons = completedLessons;
    }
}
