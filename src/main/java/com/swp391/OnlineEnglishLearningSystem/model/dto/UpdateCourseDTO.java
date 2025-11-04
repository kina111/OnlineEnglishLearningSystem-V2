package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class UpdateCourseDTO {
    @NotBlank(message = "Vui lòng nhập tên khóa học.")
    @Size(min = 5, max = 100, message = "Tên khóa học phải từ 5 đến 100 ký tự.")
    private String name;

    @NotBlank(message = "Vui lòng nhập mô tả ngắn.")
    @Size(min = 10, max = 200, message = "Mô tả ngắn phải từ 10 đến 200 ký tự.")
    private String shortDescription;

    @NotBlank(message = "Vui lòng nhập mô tả chi tiết.")
    @Size(min = 10, max = 1000, message = "Mô tả chi tiết phải từ 10 đến 1000 ký tự.")
    private String description;

    @NotBlank(message = "Vui lòng nhập yêu cầu đầu vào (prerequisite).")
    @Size(min = 10, max = 10000, message = "Yêu cầu đầu vào phải từ 10 đến 10000 ký tự.")
    private String prerequisite;

    @NotNull(message = "Vui lòng nhập giá khóa học.")
    @DecimalMin(value = "0.0", message = "Giá khóa học phải lớn hơn hoặc bằng 0.")
    private Double price;

    @NotNull(message = "Vui lòng nhập mức giảm giá.")
    @DecimalMin(value = "0.0", message = "Mức giảm giá không được âm.")
    @DecimalMax(value = "100.0", message = "Mức giảm giá không được vượt quá 100%.")
    private Double discount;

    @NotNull(message = "Vui lòng chọn danh mục khóa học.")
    private Long categoryId;

    private MultipartFile thumbnailFile;

    private String currentThumbnailUrl;

    private Course.CourseStatus status;

    public UpdateCourseDTO() {
        super();
    }

    public UpdateCourseDTO(Course course) {
        this.name = course.getName();
        this.shortDescription = course.getShortDescription();
        this.description = course.getDescription();
        this.prerequisite = course.getPrerequisite();
        this.price = course.getPrice();
        this.discount = course.getDiscount();
        this.categoryId = course.getCategory().getId();
        this.thumbnailFile = null;
        this.currentThumbnailUrl = course.getThumbnail();
        this.status = course.getStatus();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    public String getCurrentThumbnailUrl() {
        return currentThumbnailUrl;
    }

    public void setCurrentThumbnailUrl(String currentThumbnailUrl) {
        this.currentThumbnailUrl = currentThumbnailUrl;
    }

    public Course.CourseStatus getStatus() {
        return status;
    }

    public void setStatus(Course.CourseStatus status) {
        this.status = status;
    }
}
