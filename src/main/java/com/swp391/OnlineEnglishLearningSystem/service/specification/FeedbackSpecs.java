package com.swp391.OnlineEnglishLearningSystem.service.specification;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class FeedbackSpecs {
    public static Specification<Feedback> searchByRatingStars(Integer ratingStars){
        return (root, query, cb) -> {
            if (ratingStars == null) {
                return cb.conjunction();
            } else {
                return cb.equal(root.get(Feedback_.RATING), ratingStars);
            }
        };
    }

    public static Specification<Feedback> containsKeyword(String keyword){
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()){
                return cb.conjunction();
            }else{
                String likePattern = "%" + keyword.toLowerCase() + "%";
                Join<Feedback, User> userJoin = root.join(Feedback_.ENROLLMENT).join(Enrollment_.USER);
                return cb.or(
                        cb.like(userJoin.get(User_.FULL_NAME), likePattern),
                        cb.like(root.get(Feedback_.RATING), likePattern)
                );
            }
        };
    }

    public static Specification<Feedback> allSpecifications(int ratingStars, String keyword){
        return Specification.allOf(searchByRatingStars(ratingStars), containsKeyword(keyword));
    }
}
