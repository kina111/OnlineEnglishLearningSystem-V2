package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseFeedbackStats;
import com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public interface FeedbackService {
    void handleSave(long enrollmentId, FeedbackRequest feedbackRequest);

    Page<FeedbackDTO> getApprovedFeedbacks(Long courseId, PageRequest createdAt);

    CourseFeedbackStats getFeedbackStats(Long courseId);

    Page<FeedbackDTO> getApprovedFeedbacksWithSpecs(Long courseId, Pageable pageable, Integer ratingFilter, String searchKeyword);
}
