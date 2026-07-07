package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_lesson_progress", uniqueConstraints = {
        // Ensure only one progress record per user, per lesson, per enrollment
        @UniqueConstraint(columnNames = {"user_id", "lesson_id", "enrollment_id"})
})
public class UserLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment; // Link back to the specific enrollment record

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted; // Default to not completed

    @Column(name = "completion_date")
    private LocalDateTime completionDate; // Nullable, set when isCompleted becomes true

    // Convenience constructor — giữ nguyên logic isCompleted = false
    public UserLesson(User user, Lesson lesson, Enrollment enrollment) {
        this.user = user;
        this.lesson = lesson;
        this.enrollment = enrollment;
        this.isCompleted = false; // Initial state
    }

    // Giữ thủ công setter tên setCompleted (không phải setIsCompleted)
    // Lombok @Setter trên boolean isCompleted sẽ sinh setCompleted đúng, nhưng để rõ ràng:
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}