package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Chapter name is required")
    @Size(min = 5, max = 100, message = "Chapter name must be between 5-100 characters")
    private String name;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    @NotBlank(message = "Short description is required")
    @Size(min = 10, max = 200, message = "Short description must be between 10-200 characters")
    private String shortDescription;

    @Column(nullable = false, name = "order_number")
    @NotBlank(message = "Order number is required")
    private Long orderNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public Chapter() {
        super();
    }

    public Chapter(String name, String shortDescription, Long orderNum) {
        super();
        this.name = name;
        this.shortDescription = shortDescription;
        this.orderNum = orderNum;
    }

    public Chapter(String name, String shortDescription, Long orderNum, Course course) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.orderNum = orderNum;
        this.course = course;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
