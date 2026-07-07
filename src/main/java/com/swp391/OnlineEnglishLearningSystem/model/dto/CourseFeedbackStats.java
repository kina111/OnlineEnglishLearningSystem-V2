package com.swp391.OnlineEnglishLearningSystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CourseFeedbackStats {
    private int totalFeedbacks;
    private double averageRating;
    Map<Integer, Integer> ratingCountMap;
}
