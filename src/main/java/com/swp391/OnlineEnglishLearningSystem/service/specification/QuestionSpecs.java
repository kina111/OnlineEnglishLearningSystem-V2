package com.swp391.OnlineEnglishLearningSystem.service.specification;

import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.model.Question_;
import org.springframework.data.jpa.domain.Specification;

public class QuestionSpecs {
    public static Specification<Question> findByType(String type) {
        return (root, query, cb) -> {
            if (type == null || type.trim().isEmpty()) {
                return cb.conjunction();
            } else {
                return cb.equal(root.get(Question_.QUESTION_TYPE), type);
            }
        };
    }
}
