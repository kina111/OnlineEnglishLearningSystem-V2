package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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

    public Chapter(String name, String shortDescription, int orderNumber) {
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
}
