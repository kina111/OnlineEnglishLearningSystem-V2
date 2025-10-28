package com.swp391.OnlineEnglishLearningSystem.model.dto;

public class EnrollmentLearningDTO {
    private Long id;
    private String title;
    private int progress;
    private int totalLessons;
    private int completedLessons;

    public EnrollmentLearningDTO(Long id, String title, int progress, int totalLessons, int completedLessons) {
        this.id = id;
        this.title = title;
        this.progress = progress;
        this.totalLessons = totalLessons;
        this.completedLessons = completedLessons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }
}
