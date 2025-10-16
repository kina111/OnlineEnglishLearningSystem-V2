package com.swp391.OnlineEnglishLearningSystem.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidMediaTypeValidator.class) // Liên kết với logic validator
@Target({ElementType.TYPE}) // Quan trọng: Chỉ định annotation này dùng cho CLASS
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMediaType {

    // Thông báo lỗi mặc định
    String message() default "Loại file upload không khớp với MediaType đã chọn.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
