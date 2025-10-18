package com.swp391.OnlineEnglishLearningSystem.service.specification;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.model.Course_;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecs {
    public static Specification<Course> hasStatus(Course.CourseStatus status) {
        return (root, query, criteriaBuilder) -> {
            // Nếu status là null, không áp dụng điều kiện lọc này
            if (status == null) {
                return criteriaBuilder.conjunction(); // Tương đương với WHERE 1=1 (luôn đúng)
            }
            // Tạo điều kiện: root.get("status") == status
            // "status" là tên của thuộc tính trong class Course
            Predicate statusPredicate = criteriaBuilder.equal(root.get(Course_.STATUS), status);
            return statusPredicate;
        };
    }

    public static Specification<Course> hasNameContaining(String keyword) {
        return ((root, query, criteriaBuilder) ->{
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get(Course_.NAME), "%" + keyword + "%");
        });
    }

    public static Specification<Course> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }
            // Thực hiện JOIN đến bảng CourseCategory thông qua trường "category"
            Join<Course, CourseCategory> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("id"), categoryId);
        };
    }

    public static Specification<Course> hasAuthorId(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            if (authorId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Course, User> authorJoin = root.join("author");
            return criteriaBuilder.equal(authorJoin.get("id"), authorId);
        };
    }
}
