package com.swp391.OnlineEnglishLearningSystem.model.dto;

import java.util.Map;

public class CourseFeedbackStats {
    private int totalFeedbacks;
    private double averageRating;
    Map<Integer, Integer> ratingCountMap;

    public CourseFeedbackStats(int totalFeedbacks, double averageRating, Map<Integer, Integer> ratingCountMap) {
        this.totalFeedbacks = totalFeedbacks;
        this.averageRating = averageRating;
        this.ratingCountMap = ratingCountMap;
    }

    public int getTotalFeedbacks() {
        return totalFeedbacks;
    }

    public void setTotalFeedbacks(int totalFeedbacks) {
        this.totalFeedbacks = totalFeedbacks;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Map<Integer, Integer> getRatingCountMap() {
        return ratingCountMap;
    }

    public void setRatingCountMap(Map<Integer, Integer> ratingCountMap) {
        this.ratingCountMap = ratingCountMap;
    }
}
