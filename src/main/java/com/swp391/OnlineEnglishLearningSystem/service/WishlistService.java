package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.Wishlist;
import java.util.List;
import java.util.Optional;

public interface WishlistService {
    Wishlist createNew(Long userId, Long courseId);

    List<Wishlist> findByUserId(Long userId);

    void delete(Long wishlistId);

    Optional<Wishlist> findByUserIdAndCourseId(Long userId, Long courseId);
}
