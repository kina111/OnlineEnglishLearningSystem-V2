package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "Tên chương không được để trống.")
    @Size(min = 5, max = 100, message = "Tên chương phải có độ dài từ 5 đến 100 ký tự.")
    private String name;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    @NotBlank(message = "Mô tả ngắn không được để trống.")
    @Size(min = 10, max = 200, message = "Mô tả ngắn phải có độ dài từ 10 đến 200 ký tự.")
    private String shortDescription;

    @Column(nullable = false, name = "order_number")
    private int orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    public Chapter() {
        super();
    }

    public Chapter(String name, String shortDescription, int orderNumber) {
        super();
        this.name = name;
        this.shortDescription = shortDescription;
        this.orderNumber = orderNumber;
    }

    public Chapter(String name, String shortDescription, int orderNumber, Course course) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.orderNumber = orderNumber;
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

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
