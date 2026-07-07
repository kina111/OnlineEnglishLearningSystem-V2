package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.Feedback;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseFeedbackStats;
import com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackRequest;
import com.swp391.OnlineEnglishLearningSystem.repository.EnrollmentRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.FeedbackRepository;
import com.swp391.OnlineEnglishLearningSystem.service.FeedbackService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final EnrollmentRepository enrollmentRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public void handleSave(long enrollmentId, FeedbackRequest feedbackRequest) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        Feedback fb = this.feedbackRepository.findByEnrollment(enrollment);
        //nếu có rồi, k tạo mới nữa
        if (fb != null) {
            fb.setRating(feedbackRequest.getRating());
            fb.setReview(feedbackRequest.getReview());
            fb.setHelpfulCount(0);
            fb.setNotHelpfulCount(0);
        }else{
            fb = new Feedback(feedbackRequest, enrollment);
        }
        this.feedbackRepository.save(fb);
    }

    @Override
    public Page<FeedbackDTO> getApprovedFeedbacks(Long courseId, PageRequest createdAt) {
        return this.feedbackRepository.findApprovedFeedbackByCourseId(courseId, createdAt);
    }

    @Override
    public CourseFeedbackStats getFeedbackStats(Long courseId) {
        List<Feedback> feedbacks = this.feedbackRepository.findByCourseId(courseId);

        double totalStars = 0;
        Map<Integer, Integer> ratingCountMap = new HashMap<>();
        for (int i = 1; i <= 5; i++) { ratingCountMap.put(i, 0);}

        for (Feedback feedback : feedbacks) {
            int rating = feedback.getRating();
            ratingCountMap.put(rating, ratingCountMap.get(rating) + 1);
            totalStars += rating;
        }

        double averageStars = feedbacks.isEmpty() ? 0 : totalStars / feedbacks.size();
        return new CourseFeedbackStats(feedbacks.size(), averageStars, ratingCountMap);
    }

    @Override
    public Page<FeedbackDTO> getApprovedFeedbacksWithSpecs(Long courseId, Pageable pageable, Integer ratingFilter, String searchKeyword) {
        return this.feedbackRepository.findFilteredFeedbackDTOs(courseId, ratingFilter, searchKeyword, pageable);
    }
}
