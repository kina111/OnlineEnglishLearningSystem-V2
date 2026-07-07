package com.swp391.OnlineEnglishLearningSystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    public BlogDTO(Long id, String authorAvatarUrl, String authorName, String title,
                   String shortDescription, String content, LocalDateTime createdDate,
                   LocalDateTime updatedDate, String thumbnail, String categoryName, String categorySlug) {
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
}
