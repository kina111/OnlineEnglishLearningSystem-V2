package com.swp391.OnlineEnglishLearningSystem.service.specification;

import org.springframework.data.jpa.domain.Specification;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.User.Gender;

public class UserSpecs {

    public static Specification<User> searchByGender(String gender) {
        return (root, query, cb) -> {
            if (gender == null || gender.equals("ALL")) {
                return cb.conjunction();
            }
            try {
                Gender genderEnum = Gender.valueOf(gender);
                return cb.equal(root.get("gender"), genderEnum);
            } catch (IllegalArgumentException e) {
                return cb.conjunction();
            }
        };
    }

    public static Specification<User> searchByRole(String roleName) {
        return (root, query, cb) -> {
            if ((roleName == null) || roleName.equals("ALL") || roleName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("role").get("name"), roleName);
        };
    }

    public static Specification<User> searchByEnabled(Boolean enabled) {
        return (root, query, cb) -> {
            if (enabled == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("enabled"), enabled);
        };
    }

    public static Specification<User> searchByEnabled(String enabled) {
        return (root, query, cb) -> {
            if (enabled == null || enabled.equals("ALL")) {
                return cb.conjunction();
            }
            Boolean enabledBool = Boolean.valueOf(enabled);
            return cb.equal(root.get("enabled"), enabledBool);
        };
    }

    public static Specification<User> searchByEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.trim().isEmpty()) {
                return cb.conjunction();
            }
            String likePattern = "%" + email.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email")), likePattern);
        };
    }

    public static Specification<User> searchByFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.trim().isEmpty()) {
                return cb.conjunction();
            }
            String likePattern = "%" + fullName.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("fullName")), likePattern);
        };
    }

    public static Specification<User> searchByMobile(String mobile) {
        return (root, query, cb) -> {
            if (mobile == null || mobile.trim().isEmpty()) {
                return cb.conjunction();
            }
            String likePattern = "%" + mobile + "%";
            return cb.like(root.get("mobile"), likePattern);
        };
    }

    public static Specification<User> searchByContainingEmailOrFullNameOrMobileKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("email")), likePattern),
                    cb.like(cb.lower(root.get("fullName")), likePattern),
                    cb.like(root.get("mobile"), "%" + keyword + "%")
            );
        };
    }

    // Method để kết hợp tất cả các specifications
    public static Specification<User> withFilters(String keyword, String gender, String role, Boolean enabled) {
        return Specification.allOf(searchByContainingEmailOrFullNameOrMobileKeyword(keyword))
                .and(searchByGender(gender))
                .and(searchByRole(role))
                .and(searchByEnabled(enabled));
    }

    public static Specification<User> withFilters(String keyword, String gender, String role, String enabled) {
        Boolean enabledBool = null;
        if (enabled != null && !enabled.equals("ALL")) {
            enabledBool = Boolean.valueOf(enabled);
        }
        return withFilters(keyword, gender, role, enabledBool);
    }
}