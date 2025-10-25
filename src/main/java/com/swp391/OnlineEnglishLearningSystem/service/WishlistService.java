package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.Wishlist;
import java.util.List;
import java.util.Optional;

public interface WishlistService {
    Wishlist createNew(User currentUser, Course currentCourse);
    List<Wishlist> findByUser(User user);

    Optional<Wishlist> findByUserAndCourse(User currentUser, Course currentCourse);

    void delete(Wishlist wishlist);
}
