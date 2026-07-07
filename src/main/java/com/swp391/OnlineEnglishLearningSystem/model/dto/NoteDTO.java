package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Note;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
