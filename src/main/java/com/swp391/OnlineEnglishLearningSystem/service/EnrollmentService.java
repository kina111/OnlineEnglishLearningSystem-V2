package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.EnrollmentLearningDTO;

import java.util.List;

public interface EnrollmentService {
    Enrollment createNew(Order order);

    List<Enrollment> findByUserId(long userId);

    Enrollment findByIdAndUserIdWithCourse(long enrollmentId, long userId);

    Enrollment findByIdAndUserIdWithFullCourseStructure(long enrollmentId, long userId);

    Enrollment findByIdWithUserLesson(long enrollmentId);

    EnrollmentLearningDTO createEnrollmentDTO(long enrollmentId);
}
