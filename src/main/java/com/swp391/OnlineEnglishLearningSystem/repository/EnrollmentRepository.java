package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.dto.EnrollmentInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Enrollment findByCourseId(Long courseId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course WHERE e.user = :user")
    List<Enrollment> findByUserWithCourse(@Param("user") User user);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course WHERE e.id = :id and e.user.id = :userId")
    Optional<Enrollment> findByIdAndUserIdWithCourse(@Param("id") Long id, @Param("userId") Long userId);

    @Query("select e from Enrollment e join fetch e.userLessons where e.id = :id")
    Optional<Enrollment> findByIdWithUserLesson(@Param("id") Long id);

    @Query("SELECT DISTINCT e FROM Enrollment e " +
            "JOIN FETCH e.course c " +
            "LEFT JOIN FETCH c.chapters chap " +
            "LEFT JOIN FETCH chap.lessons l " +
            "WHERE e.id = :enrollmentId AND e.user.id = :userId") // Thêm check userId cho an toàn
    Optional<Enrollment> findByIdAndUserIdWithFullCourseStructure(@Param("enrollmentId") long enrollmentId, @Param("userId") long userId);

    List<Enrollment> findByUserId(long userId);

    @Query("SELECT NEW com.swp391.OnlineEnglishLearningSystem.model.dto.EnrollmentInfoDTO( " +
            "    e.id, " +
            "    c.name, " +
            "    c.thumbnail, " +
            "    c.shortDescription, " +
            "    e.completedAt, " +
            "    e.lastAccessAt, " +
            "    e.enrolledAt, " +
            "    COALESCE(COUNT(ul), 0), " +
            "    COALESCE(SUM(CASE WHEN ul.isCompleted = true THEN 1 ELSE 0 END), 0) " +
            ") " +
            "FROM Enrollment e " +
            "JOIN e.course c " +
            "JOIN e.user u " +
            "LEFT JOIN e.userLessons ul " +
            "WHERE u.id = :userId " +
            "GROUP BY e.id, c.name, c.thumbnail, c.shortDescription, e.completedAt, e.lastAccessAt, e.enrolledAt")
    List<EnrollmentInfoDTO> findEnrollmentInfoByUserId(@Param("userId") Long userId);
}
