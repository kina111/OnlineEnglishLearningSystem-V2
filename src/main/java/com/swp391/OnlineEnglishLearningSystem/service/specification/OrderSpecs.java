package com.swp391.OnlineEnglishLearningSystem.service.specification;

import com.swp391.OnlineEnglishLearningSystem.model.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class OrderSpecs {

    public static Specification<Order> fromDaysAgo(int daysAgo) {
        return (root, query, criteriaBuilder) ->{
            LocalDateTime from =LocalDateTime.now().minusDays(daysAgo);
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), from);
        };
    }

    public static Specification<Order> fromMonthsAgo(int monthsAgo) {
        return ((root, query, criteriaBuilder) -> {
            LocalDateTime from =LocalDateTime.now().minusMonths(monthsAgo);
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), from);
        });
    }

    public static Specification<Order> fromCreateDate(LocalDateTime date) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), date);
        });
    }
    public static Specification<Order> toCreateDate(LocalDateTime date) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), date);
        });
    }

    public static Specification<Order> fromUpdateDate(LocalDateTime date) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), date);
        });
    }
    public static Specification<Order> toUpdateDate(LocalDateTime date) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), date);
        });
    }

    public static Specification<Order> hasStatus(String status) {
        return ((root, query, criteriaBuilder) ->  {
           return criteriaBuilder.equal(root.get("status"), status);
        });
    }

    public static Specification<Order> hasOrderCode(String code) {
        return ((root, query, criteriaBuilder) ->  {
            return criteriaBuilder.like(root.get("orderCode"), code);
        });
    }

//    public static Specification<Order> hasUserId(String UserId) {
//        return ((root, query, criteriaBuilder) -> {
//            return criteriaBuilder.equal(root.get("userId"), UserId);
//        });
//    }

}
