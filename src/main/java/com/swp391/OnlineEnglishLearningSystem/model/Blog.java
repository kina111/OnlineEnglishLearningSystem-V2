package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "blogs")
public class Blog extends BaseEntity {
    public enum BlogStatus {
        PUBLISHED, DRAFT, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Size(min = 5, max = 100, message = "Tiêu đề phải có độ dài từ 5 đến 100 ký tự.")
    private String title;

    @Column(name = "thumbnail_url")
    @NotNull(message = "Ảnh bìa bài viết không được để trống.")
    private String thumbnail;

    @Column(name = "short_description", columnDefinition = "NVARCHAR(255)")
    @Size(min = 10, max = 200, message = "Mô tả ngắn phải có độ dài từ 10 đến 200 ký tự.")
    private String shortDescription;

    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)")
    @Size(min = 10, message = "Nội dung bài viết tối thiểu 10 kí tự.")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "Trạng thái bài viết không được để trống.")
    private BlogStatus status = BlogStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_category_id")
    private BlogCategory blogCategory;

    // 2. Liên kết với Tác giả
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") // Tên cột Foreign Key trong CSDL
    private User author;

    public Blog(Long id, String title, String thumbnail, String shortDescription, String content,
                BlogCategory blogCategory, User author) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.shortDescription = shortDescription;
        this.content = content;
        this.blogCategory = blogCategory;
        this.author = author;
    }
}
