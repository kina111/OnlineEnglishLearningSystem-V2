package com.swp391.OnlineEnglishLearningSystem.service;


import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import jakarta.validation.Valid;

public interface CourseService {
    Course buildNewCourse(@Valid CourseDTO courseDTO);

    void save(Course newCourse);

    Course findById(Long id);
}
