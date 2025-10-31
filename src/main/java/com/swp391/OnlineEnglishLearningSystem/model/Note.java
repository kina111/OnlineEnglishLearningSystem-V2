package com.swp391.OnlineEnglishLearningSystem.model;

import com.swp391.OnlineEnglishLearningSystem.model.dto.NoteRequest;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "notes")
public class Note extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "time_at_lesson")
    private String timeAtLesson;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)", name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_lesson_id")
    private UserLesson userLesson;

    public Note() {
    }

    public Note(UserLesson userLesson, NoteRequest noteRequest) {
        this.timeAtLesson = noteRequest.getTimeAtLesson();
        this.content = noteRequest.getContent();
        this.userLesson = userLesson;
    }

    public Note(String timeAtLesson, String content, UserLesson userLesson) {
        this.timeAtLesson = timeAtLesson;
        this.content = content;
        this.userLesson = userLesson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimeAtLesson() {
        return timeAtLesson;
    }

    public void setTimeAtLesson(String timeAtLesson) {
        this.timeAtLesson = timeAtLesson;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserLesson getUserLesson() {
        return userLesson;
    }

    public void setUserLesson(UserLesson userLesson) {
        this.userLesson = userLesson;
    }
}
