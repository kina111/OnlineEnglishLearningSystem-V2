package com.swp391.OnlineEnglishLearningSystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SliderCreateUpdateDto {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private Integer orderNumber;
    private String status; // SHOW / HIDE
    private String linkUrl;
    private MultipartFile imageFile;
}
