package com.swp391.OnlineEnglishLearningSystem.service;


import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UpdateCourseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    Course buildNewCourse(@Valid CourseDTO courseDTO, Long authorId);

    void save(Course newCourse);

    Course findById(Long id);

    Page<Course> findCoursesByAuthorAndFilters(Long userId, Course.CourseStatus status, Long categoryId, String keyword, Pageable pageable);

    Course updateCourse(Long id, @Valid UpdateCourseDTO updateDto);

    void sendSubmitReview(Long courseId);

    Course deleteById(Long courseId);

    Course handleChangingCourseStatus(Long courseId, String respond);

    void updateFeaturedStatus(Long courseId, Boolean featured);

    List<Course> findFeaturedCourses(int quantity);
}
