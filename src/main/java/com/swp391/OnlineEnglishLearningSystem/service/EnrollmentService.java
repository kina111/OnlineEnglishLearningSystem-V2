package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import java.util.List;

public interface EnrollmentService {
    Enrollment createNew(Order order);

    List<Enrollment> findByUser(User user);
}
