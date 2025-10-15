package com.swp391.OnlineEnglishLearningSystem.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
// Đây là dòng quan trọng nhất, nó liên kết annotation này với lớp xử lý logic
@Constraint(validatedBy = AtLeastOneCorrectAnswerValidator.class)
// Annotation này có thể được dùng trên trường (FIELD) hoặc phương thức (METHOD)
@Target({ElementType.FIELD, ElementType.METHOD})
// Annotation này sẽ được giữ lại lúc runtime để framework có thể xử lý
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneCorrectAnswer {

    // Thông báo lỗi mặc định khi validation thất bại
    String message() default "Phải có ít nhất một đáp án đúng";

    // Boilerplate bắt buộc cho mọi custom validation annotation
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}