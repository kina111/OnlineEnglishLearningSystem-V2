package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);

    @Query("SELECT w FROM Wishlist w JOIN FETCH w.course WHERE w.user = :user")
    List<Wishlist> findByUserWithCourse(@Param("user") User user);

    Optional<Wishlist> findByUserAndCourse(User user, Course course);
}
