package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.Feedback;
import com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findByEnrollment(Enrollment enrollment);

    @Query("SELECT NEW com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackDTO( " +
            "    f.id, " +
            "    u.fullName, " +
            "    u.avatar, " +
            "    f.rating, " +
            "    f.review, " +
            "    f.helpfulCount, " +
            "    f.createdAt " +
            ") " +
            "FROM Feedback f " +
            "JOIN f.enrollment e " +
            "JOIN e.user u " +
            "WHERE e.course.id = :courseId AND f.status = 'APPROVED' ")
    Page<FeedbackDTO> findApprovedFeedbackByCourseId(
            @Param("courseId") Long courseId,
            Pageable pageable
    );

    @Query("SELECT f FROM Feedback f WHERE f.enrollment.course.id = :courseId")
    List<Feedback> findByCourseId(Long courseId);

    Page<Feedback> findAll(Specification<Feedback> feedbackSpecification, Pageable pageable);
    @Query("SELECT NEW com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackDTO( " +
            "    f.id, " +
            "    u.fullName, " +
            "    u.avatar, " +
            "    f.rating, " +
            "    f.review, " +
            "    f.helpfulCount, " +
            "    f.createdAt " +
            ") " +
            "FROM Feedback f " +
            "JOIN f.enrollment e " +
            "JOIN e.user u " +
            "WHERE e.course.id = :courseId " +
            "AND f.status = 'APPROVED' " +
            "AND (:rating IS NULL OR f.rating = :rating) " +
            "AND (:keyword IS NULL OR f.review LIKE %:keyword% OR u.fullName LIKE %:keyword%)"
    )
    Page<FeedbackDTO> findFilteredFeedbackDTOs(
            @Param("courseId") Long courseId,
            @Param("rating") Integer rating, // null nếu "Tất cả"
            @Param("keyword") String keyword, // null nếu không tìm kiếm
            Pageable pageable
    );

}
