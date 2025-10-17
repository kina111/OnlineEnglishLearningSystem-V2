package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.controller.ChapterController;
import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import jakarta.validation.Valid;

import java.util.Optional;

public interface ChapterService {
    Chapter createChapterForCourse(Long courseId, ChapterController.CreateChapterRequest createChapterRequest);

    Optional<Chapter> findById(Long chapterId);

    void deleteById(Long chapterId);

    void deleteChapterAndReorder(Long courseId, Long chapterId);

    Chapter updateChapter(Long chapterId, ChapterController.CreateChapterRequest chapter);
}
