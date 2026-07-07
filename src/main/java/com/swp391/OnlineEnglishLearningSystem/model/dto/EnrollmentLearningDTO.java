package com.swp391.OnlineEnglishLearningSystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EnrollmentLearningDTO {
    private Long id;
    private String title;
    private int progress;
    private int totalLessons;
    private int completedLessons;
}
