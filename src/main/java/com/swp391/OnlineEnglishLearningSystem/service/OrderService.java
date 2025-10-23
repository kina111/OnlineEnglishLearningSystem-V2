package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User;

import java.util.Map;

public interface OrderService {
    Order createNewOrder(User currentUser, Course currentCourse);

    /*String processIPN(Map<String, String> vnpParams);*/

  /*  // BỔ SUNG: Hàm xử lý logic cho Return URL
    Order processReturn(Map<String, String> vnpParams);*/

    Order update(Map fields);
}
