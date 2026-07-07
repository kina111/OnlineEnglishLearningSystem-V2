package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.util.ValidMediaType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@ValidMediaType(message = "Vui lòng tải file hợp lệ.")
public class CourseDTO {
    @NotBlank(message = "Vui lòng nhập tên khóa học")
    @Size(min = 5, max = 100, message = "Tên khóa học phải từ 5-100 kí tự")
    private String name;

    @NotBlank(message = "Vui lòng nhập mô tả ngắn")
    @Size(min = 10, max = 200, message = "Mô tả ngắn phải từ 10-200 kí tự")
    private String shortDescription;

    @NotBlank(message = "Vui lòng nhập mô tả")
    @Size(min = 10, message = "Mô tả chi tiết phải từ 10 kí tự")
    private String description;

    @NotBlank(message = "Vui lòng nhập yêu cầu tiên quyết")
    //@Size(min = 10, message = "Prerequisite must be between 10-10000 characters")
    private String prerequisite;

    @NotNull(message = "Vui lòng nhập giá")
    @DecimalMin(value = "1.0", message = "Giá tiền phải là số dương")
    private Double price;

    @NotNull(message = "Vui lòng nhập giảm giá")
    @DecimalMin(value = "0.0", message = "Giảm giá phải từ 0-100")
    @DecimalMax(value = "100.0", message = "Giảm giá phải từ 0-100")
    private Double discount;

    @NotNull(message = "Vui lòng chọn danh mục")
    private Long categoryId;

    @NotNull(message = "Vui lòng chọn thumbnail")
    private MultipartFile thumbnailFile;

    private Course.CourseStatus status;

    // Giữ constructor có tham số: tự set status = DRAFT
    public CourseDTO(String name, String description, String prerequisite, Double price, Double discount,
                     Long categoryId, MultipartFile thumbnailFile) {
        this.name = name;
        this.description = description;
        this.prerequisite = prerequisite;
        this.price = price;
        this.discount = discount;
        this.categoryId = categoryId;
        this.thumbnailFile = thumbnailFile;
        this.status = Course.CourseStatus.DRAFT;
    }
}
