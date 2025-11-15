package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.util.ValidMediaType;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

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

    public CourseDTO() {
        super();
    }
    public CourseDTO(String name, String description, String prerequisite, Double price, Double discount, Long categoryId, MultipartFile thumbnailFile) {
        super();
        this.name = name;
        this.description = description;
        this.prerequisite = prerequisite;
        this.price = price;
        this.discount = discount;
        this.categoryId = categoryId;
        this.thumbnailFile = thumbnailFile;
        this.status = Course.CourseStatus.DRAFT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public MultipartFile getThumbnailFile() {
        return thumbnailFile;
    }

    public void setThumbnailFile(MultipartFile thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Course.CourseStatus getStatus() {
        return status;
    }

    public void setStatus(Course.CourseStatus status) {
        this.status = status;
    }
}
