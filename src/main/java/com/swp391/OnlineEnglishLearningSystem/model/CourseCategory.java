package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_categories")
public class CourseCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "Tên danh mục khóa học không được để trống.")
    @Size(min = 5, max = 100, message = "Tên danh mục khóa học phải có độ dài từ 5 đến 100 ký tự.")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    @NotBlank(message = "Mô tả danh mục không được để trống.")
    @Size(min = 10, max = 1000, message = "Mô tả danh mục phải có độ dài từ 10 đến 1000 ký tự.")
    private String description;

    @Column(nullable = false, name = "active")
    private boolean active;

    // Giữ no-args constructor vì có logic init: active = false
    public CourseCategory() {
        active = false;
    }

    public CourseCategory(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }
}
