package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Enrollment findByCourseId(Long courseId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course WHERE e.user = :user")
    List<Enrollment> findByUserWithCourse(@Param("user") User user);
}
