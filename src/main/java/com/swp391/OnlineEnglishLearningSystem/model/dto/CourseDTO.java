package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class CourseDTO {
    @NotBlank(message = "Course name is required")
    @Size(min = 5, max = 100, message = "Course name must be between 5-100 characters")
    private String name;

    @NotBlank(message = "Short description is required")
    @Size(min = 10, message = "Short description must be between 10-200 characters")
    private String shortDescription;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be between 10-1000 characters")
    private String description;

    @NotBlank(message = "Prerequisite is required")
    @Size(min = 10, message = "Prerequisite must be between 10-10000 characters")
    private String prerequisite;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    private Double price;

    @NotNull(message = "Discount is required")
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount cannot exceed 100%")
    private Double discount;

    @NotNull(message = "Course category is required")
    private Long categoryId;

    @NotNull(message = "Course thumbnail is required")
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
