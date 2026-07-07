package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
        this.numberOfCompletedLesson = (int) userLessonLearningDTOS.stream()
                .filter(UserLessonLearningDTO::isCompleted).count();
        this.totalDuration = userLessonLearningDTOS.stream()
                .mapToLong(UserLessonLearningDTO::getDuration).sum();
        this.userLessonLearningDTOS = userLessonLearningDTOS;
    }
}
