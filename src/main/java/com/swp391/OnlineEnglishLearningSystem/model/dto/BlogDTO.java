package com.swp391.OnlineEnglishLearningSystem.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BlogDTO {
    private Long id;
    private String authorAvatarUrl;
    private String authorName;
    private String title;
    private String shortDescription;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String thumbnail;
    private String categoryName;
    private String categorySlug;

    public BlogDTO() {
    }

    public BlogDTO(Long id, String authorAvatarUrl, String authorName, String title, String shortDescription, String content, LocalDateTime createdDate, LocalDateTime updatedDate, String thumbnail, String categoryName, String categorySlug) {
        super();
        this.id = id;
        this.authorAvatarUrl = authorAvatarUrl;
        this.authorName = authorName;
        this.title = title;
        this.shortDescription = shortDescription;
        this.content = content;
        this.createdAt = createdDate;
        this.updatedAt = updatedDate;
        this.thumbnail = thumbnail;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    public void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }
}
