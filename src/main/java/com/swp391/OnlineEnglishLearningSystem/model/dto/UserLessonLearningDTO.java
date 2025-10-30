package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.UserLesson;

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
        this.duration = lesson.getLessonType() == Lesson.LessonType.LECTURE ? lesson.getDuration()/1000 : lesson.getTimeLimitInMinutes() * 60;
        this.isCompleted = userLesson.isCompleted();
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public int getOrderInChapter() {
        return orderInChapter;
    }

    public void setOrderInChapter(int orderInChapter) {
        this.orderInChapter = orderInChapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Lesson.LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(Lesson.LessonType lessonType) {
        this.lessonType = lessonType;
    }
}
