package com.swp391.OnlineEnglishLearningSystem.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course extends BaseEntity{
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
    @Size(min = 10, max = 1000, message = "Mô tả chi tiết phải có độ dài từ 10 đến 1000 ký tự.")
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
    private CourseStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CourseCategory category;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    // Constructors - ĐÃ ĐƯỢC TỐI ƯU
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

    public Course(String name, String shortDescription, String description, String prerequisite, String thumbnail, Double price, Double discount, boolean featured, CourseStatus status, int totalLesson, CourseCategory category) {
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

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
