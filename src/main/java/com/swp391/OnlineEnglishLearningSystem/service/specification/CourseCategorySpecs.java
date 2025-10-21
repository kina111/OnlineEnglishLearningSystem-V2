package com.swp391.OnlineEnglishLearningSystem.service.specification;

import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory_;
import org.springframework.data.jpa.domain.Specification;

public class CourseCategorySpecs {
    public static Specification<CourseCategory> searchByName(String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return cb.conjunction();
            } else {
                return cb.like(root.get(CourseCategory_.NAME), "%" + name + "%");
            }
        };
    }

    public static Specification<CourseCategory> searchByactive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }else{
                return criteriaBuilder.equal(root.get(CourseCategory_.ACTIVE), active);
            }
        };
    }
}
