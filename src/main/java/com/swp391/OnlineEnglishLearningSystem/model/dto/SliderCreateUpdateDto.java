package com.swp391.OnlineEnglishLearningSystem.model.dto;


import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class SliderCreateUpdateDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private Integer orderNumber;
    private String status; // SHOW / HIDE
    private String linkUrl;


    private MultipartFile imageFile;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

}

