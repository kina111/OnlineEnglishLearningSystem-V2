package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.Order;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.OrderFilter;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order createNewOrder(User currentUser, Course currentCourse);

    /*String processIPN(Map<String, String> vnpParams);*/

  /*  // BỔ SUNG: Hàm xử lý logic cho Return URL
    Order processReturn(Map<String, String> vnpParams);*/

    Order update(Map fields);
    List<Order> getAllOrders();
    List<Order> getOrdersWithSpecs(OrderFilter filter);
    Order getOrderById(Long id);
}
