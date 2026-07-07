package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.UserLesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLessonLearningDTO {
    private Long lessonId;
    private int orderInChapter;
    private String title;
    private long duration; //in seconds
    private boolean isCompleted;
    private Lesson.LessonType lessonType;

    public UserLessonLearningDTO(UserLesson userLesson) {
        Lesson lesson = userLesson.getLesson();
        this.lessonId = lesson.getId();
        this.orderInChapter = lesson.getOrderNumber();
        this.title = lesson.getTitle();
        this.lessonType = lesson.getLessonType();
        this.duration = lesson.getLessonType() == Lesson.LessonType.LECTURE
                ? lesson.getDuration() / 1000
                : lesson.getTimeLimitInMinutes() * 60;
        this.isCompleted = userLesson.isCompleted();
    }

    // Giữ getter/setter thủ công cho isCompleted do naming convention
    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
