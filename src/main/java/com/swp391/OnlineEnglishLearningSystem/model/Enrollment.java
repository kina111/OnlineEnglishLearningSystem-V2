package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_id"})
})
public class Enrollment {

    public enum EnrollmentStatus {
        ENROLLED("Đã đăng kí"),
        COMPLETED("Hoàn thành"),
        CANCELLED("Hủy đăng kí");

        private final String displayName;

        EnrollmentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "enrollment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLesson> userLessons = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "last_access_at")
    private LocalDateTime lastAccessAt;

    @NotNull
    @Enumerated(EnumType.STRING) //lưu tên Enum (ENROLLED, COMPLETED) vào db
    @Column(nullable = false)
    private EnrollmentStatus status;

    // Giữ nguyên constructor: có logic tự set status = ENROLLED
    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
        this.status = EnrollmentStatus.ENROLLED;
    }
}
