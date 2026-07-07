package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateLectureRequest {
    @NotBlank(message = "Vui lòng nhập tên bài học")
    @Size(min = 5, max = 200, message = "Tên bài học trong khoảng 5-200 kí tự")
    private String title;

    // --- Lecture fields ---
    @NotNull(message = "Vui lòng nhập thời lượng ước tính")
    @Positive(message = "Vui lòng nhập thời lượng ước tính hợp lệ")
    private Integer estimatedTime;

    @NotBlank(message = "Nội dung bài giảng không được để trống")
    private String htmlContent;

    private MultipartFile video;
}
