package com.swp391.OnlineEnglishLearningSystem.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private String errorCode;
    private LocalDateTime timestamp = LocalDateTime.now();

    // Giữ constructor: có logic tính status từ HttpStatus
    public ApiResponse(HttpStatus httpStatus, String message, T data, String errorCode) {
        this.status = httpStatus.is2xxSuccessful() ? "success" : "error";
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
}
