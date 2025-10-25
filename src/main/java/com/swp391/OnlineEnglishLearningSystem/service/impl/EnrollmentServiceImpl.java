package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.repository.EnrollmentRepository;
import com.swp391.OnlineEnglishLearningSystem.service.EnrollmentService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public Enrollment createNew(Order order) {
        return this.enrollmentRepository.save(new Enrollment(order.getUser(), order.getCourse()));
    }

    @Override
    public List<Enrollment> findByUser(User user) {
        if (user == null) throw new IllegalArgumentException("User not found");
        return this.enrollmentRepository.findByUserWithCourse(user);
    }

    public boolean isEnrolled(Long userId, Long courseId) {
        if (userId == null || courseId == null) return false;
        Enrollment temp = this.enrollmentRepository.findByCourseId(courseId);
        if (temp == null) return false;
        return temp.getUser().getId() == (userId);
    }
}
