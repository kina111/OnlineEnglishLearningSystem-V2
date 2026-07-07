package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {
    public enum CourseStatus {
        PUBLISHED("Đã duyệt"),
        DRAFT("Đang sửa"),
        PENDING("Đang chờ duyệt");

        private final String displayName;

        CourseStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "Tên khóa học không được để trống.")
    @Size(min = 5, max = 100, message = "Tên khóa học phải có độ dài từ 5 đến 100 ký tự.")
    private String name;

    @Column(name = "short_description", nullable = false, columnDefinition = "NVARCHAR(255)")
    @NotBlank(message = "Mô tả ngắn không được để trống.")
    @Size(min = 10, max = 200, message = "Mô tả ngắn phải có độ dài từ 10 đến 200 ký tự.")
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    @NotBlank(message = "Mô tả chi tiết không được để trống.")
    @Size(min = 10, message = "Mô tả chi tiết phải có độ dài từ 10 ký tự.")
    private String description;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    @NotBlank(message = "Yêu cầu đầu vào không được để trống.")
    private String prerequisite;

    @NotNull(message = "Ảnh bìa khóa học không được để trống.")
    private String thumbnail;

    @NotNull(message = "Giá khóa học không được để trống.")
    @DecimalMin(value = "0.0", message = "Giá khóa học phải là số dương.")
    private Double price;

    @NotNull(message = "Giá trị giảm giá không được để trống.")
    @DecimalMin(value = "0.0", message = "Giảm giá không được âm.")
    @DecimalMax(value = "100.0", message = "Giảm giá không được vượt quá 100%.")
    private Double discount;

    private boolean featured;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CourseCategory category;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    // Giữ constructor mặc định: có logic init quan trọng
    public Course() {
        this.status = CourseStatus.DRAFT;
        this.featured = false;
        this.price = 0.0;
        this.discount = 0.0;
    }

    public Course(String name, String description, String prerequisite,
                  String thumbnail, double price, double discount,
                  boolean featured, CourseStatus status) {
        this(); // Gọi constructor mặc định
        this.name = name;
        this.description = description;
        this.prerequisite = prerequisite;
        this.thumbnail = thumbnail;
        this.price = price;
        this.discount = discount;
        this.featured = featured;
        this.status = status != null ? status : CourseStatus.DRAFT;
    }

    public Course(String name, String shortDescription, String description, String prerequisite,
                  String thumbnail, Double price, Double discount, boolean featured,
                  CourseStatus status, int totalLesson, CourseCategory category) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.prerequisite = prerequisite;
        this.thumbnail = thumbnail;
        this.price = price;
        this.discount = discount;
        this.featured = featured;
        this.status = status;
        this.category = category;
    }

    // Thêm constructor tiện ích
    public Course(String name, String description, double price) {
        this();
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
