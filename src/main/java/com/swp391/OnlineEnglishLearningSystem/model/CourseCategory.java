package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "course_categories")
public class CourseCategory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false, unique = true, columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "Course category name is required")
    @Size(min = 5, max = 100, message = "Course name must be between 5-50 characters")
    private String name;

    @Size(min=10, max=1000, message = "Description must be between 10-100 characters")
    @NotBlank(message = "Description is required")
    @Column(nullable = false, name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(nullable = false, name = "active")
    private boolean active;

    public CourseCategory() {
        active = false;
    }

    public CourseCategory(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
