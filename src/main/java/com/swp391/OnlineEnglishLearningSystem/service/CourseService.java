package com.swp391.OnlineEnglishLearningSystem.service;


import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    Course buildNewCourse(@Valid CourseDTO courseDTO);

    void save(Course newCourse);

    Course findById(Long id);

    Page<Course> findCoursesByAuthorAndFilters(Long userId, Course.CourseStatus status, Long categoryId, String keyword, Pageable pageable);
}
