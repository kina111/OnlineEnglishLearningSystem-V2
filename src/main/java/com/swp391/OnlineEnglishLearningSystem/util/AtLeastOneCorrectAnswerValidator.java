package com.swp391.OnlineEnglishLearningSystem.util;

import com.swp391.OnlineEnglishLearningSystem.model.AnswerOption;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class AtLeastOneCorrectAnswerValidator implements ConstraintValidator<AtLeastOneCorrectAnswer, List<AnswerOption>> {

    @Override
    public boolean isValid(List<AnswerOption> answerOptions, ConstraintValidatorContext context) {
        // Nếu danh sách là null hoặc rỗng, chắc chắn không có đáp án đúng nào -> không hợp lệ
        if (answerOptions == null || answerOptions.isEmpty()) {
            return false;
        }

        // Sử dụng Stream API để kiểm tra xem có bất kỳ phần tử nào trong list
        // có thuộc tính 'correct' là true hay không.
        // Ngay khi tìm thấy một phần tử thỏa mãn, nó sẽ trả về true ngay lập tức.
        return answerOptions.stream().anyMatch(AnswerOption::getCorrect);
    }
}
