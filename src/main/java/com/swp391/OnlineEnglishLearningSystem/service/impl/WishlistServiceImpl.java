package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.Wishlist;
import com.swp391.OnlineEnglishLearningSystem.repository.CourseRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.WishlistRepository;
import com.swp391.OnlineEnglishLearningSystem.service.WishlistService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public Wishlist createNew(Long userId, Long courseId) {
        User currentUser = this.userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Course currentCourse = this.courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        return this.wishlistRepository.save(new Wishlist(currentUser, currentCourse));
    }

    @Override
    public List<Wishlist> findByUserId(Long userId) {
        return this.wishlistRepository.findByUserId(userId);
    }

    @Override
    public void delete(Long wishlistId) {
        this.wishlistRepository.deleteById(wishlistId);
    }

    @Override
    public Optional<Wishlist> findByUserIdAndCourseId(Long userId, Long courseId) {
        return this.wishlistRepository.findByUserIdAndCourseId(userId, courseId);
    }
}
