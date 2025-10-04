package com.swp391.OnlineEnglishLearningSystem.util;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE }) // Adjusted to apply to the class level
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidEmail {

    String message() default "Invalid email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
