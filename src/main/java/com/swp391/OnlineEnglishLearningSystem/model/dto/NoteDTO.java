package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Note;

public class NoteDTO {
    private Long id;
    private String content;
    private String timeAtLesson;
    private String lessonTitle;

    public NoteDTO(Long id, String content, String timeAtLesson, String lessonTitle) {
        this.id = id;
        this.content = content;
        this.timeAtLesson = timeAtLesson;
        this.lessonTitle = lessonTitle;
    }

    public NoteDTO(Note note, String lessonTitle) {
        this.id = note.getId();
        this.content = note.getContent();
        this.timeAtLesson = note.getTimeAtLesson();
        this.lessonTitle = lessonTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeAtLesson() {
        return timeAtLesson;
    }

    public void setTimeAtLesson(String timeAtLesson) {
        this.timeAtLesson = timeAtLesson;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }
}
