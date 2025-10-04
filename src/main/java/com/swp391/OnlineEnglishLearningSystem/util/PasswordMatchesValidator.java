package com.swp391.OnlineEnglishLearningSystem.util;

import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import jakarta.validation.ConstraintValidator;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public boolean isValid(Object obj, jakarta.validation.ConstraintValidatorContext context) {
        UserDTO user = (UserDTO) obj;
        return user.getPassword().equals(user.getConfirmedPassword());
    }
}
