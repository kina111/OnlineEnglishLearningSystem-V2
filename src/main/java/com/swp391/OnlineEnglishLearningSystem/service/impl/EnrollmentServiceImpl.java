package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.UserLesson;
import com.swp391.OnlineEnglishLearningSystem.model.dto.EnrollmentInfoDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.EnrollmentLearningDTO;
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
    public List<Enrollment> findByUserId(long userId) {
        return this.enrollmentRepository.findByUserId(userId);
    }

    @Override
    public Enrollment findByIdAndUserIdWithCourse(long enrollmentId, long userId) {
        return this.enrollmentRepository.findByIdAndUserIdWithCourse(enrollmentId, userId).orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
    }

    @Override
    public Enrollment findByIdAndUserIdWithFullCourseStructure(long enrollmentId, long userId) {
        return this.enrollmentRepository.findByIdAndUserIdWithFullCourseStructure(enrollmentId, userId).orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
    }

    @Override
    public Enrollment findByIdWithUserLesson(long enrollmentId) {
        return this.enrollmentRepository.findByIdWithUserLesson(enrollmentId).orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
    }

    @Override
    public EnrollmentLearningDTO createEnrollmentDTO(long enrollmentId) {
        Enrollment enrollment = findByIdWithUserLesson(enrollmentId);
        List<UserLesson> userLessons = enrollment.getUserLessons();
        String title = enrollment.getCourse().getName();
        int totalLessons = userLessons.size();
        int completedLessons = (int) userLessons.stream().filter(UserLesson::isCompleted).count();
        return new EnrollmentLearningDTO(enrollmentId, title, completedLessons/totalLessons, totalLessons, completedLessons);
    }

    @Override
    public List<EnrollmentInfoDTO> createEnrollmentInfoDTO(Long userId) {
        List<EnrollmentInfoDTO> result = enrollmentRepository.findEnrollmentInfoByUserId(userId);
        for (EnrollmentInfoDTO enrollmentInfoDTO : result) {
            enrollmentInfoDTO.setProgress((int) (enrollmentInfoDTO.getCompletedLessons() * 100/enrollmentInfoDTO.getTotalLessons()));
        }
        return result;
    }

    @Override
    public Enrollment findByUserIdAndCourseId(Long userId, Long courseId) {
        return this.enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
    }
}
