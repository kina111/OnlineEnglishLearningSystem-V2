package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import com.swp391.OnlineEnglishLearningSystem.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChapterLearningDTO {
    private Long id;
    private int orderInCourse;
    private String title;
    private int numberOfLesson;
    private int numberOfCompletedLesson;
    private long totalDuration;
    private List<UserLessonLearningDTO> userLessonLearningDTOS = new ArrayList<>();

    public ChapterLearningDTO(Chapter chapter, List<UserLessonLearningDTO> userLessonLearningDTOS) {
        this.id = chapter.getId();
        this.orderInCourse = chapter.getOrderNumber();
        this.title = chapter.getName();
        this.numberOfLesson = chapter.getLessons().size();
        this.numberOfCompletedLesson = (int) userLessonLearningDTOS.stream().filter(UserLessonLearningDTO::isCompleted).count();
        this.totalDuration = userLessonLearningDTOS.stream().mapToLong(UserLessonLearningDTO::getDuration).sum();
        this.userLessonLearningDTOS = userLessonLearningDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderInCourse() {
        return orderInCourse;
    }

    public void setOrderInCourse(int orderInCourse) {
        this.orderInCourse = orderInCourse;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfLesson() {
        return numberOfLesson;
    }

    public void setNumberOfLesson(int numberOfLesson) {
        this.numberOfLesson = numberOfLesson;
    }

    public int getNumberOfCompletedLesson() {
        return numberOfCompletedLesson;
    }

    public void setNumberOfCompletedLesson(int numberOfCompletedLesson) {
        this.numberOfCompletedLesson = numberOfCompletedLesson;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<UserLessonLearningDTO> getUserLessonLearningDTOS() {
        return userLessonLearningDTOS;
    }

    public void setUserLessonLearningDTOS(List<UserLessonLearningDTO> userLessonLearningDTOS) {
        this.userLessonLearningDTOS = userLessonLearningDTOS;
    }
}
