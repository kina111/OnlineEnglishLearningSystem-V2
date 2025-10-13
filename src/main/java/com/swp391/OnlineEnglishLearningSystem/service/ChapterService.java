package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.controller.ChapterController;
import com.swp391.OnlineEnglishLearningSystem.model.Chapter;

import java.util.Optional;

public interface ChapterService {
    Chapter createChapterForCourse(Long courseId, ChapterController.CreateChapterRequest createChapterRequest);

    Optional<Chapter> findById(Long chapterId);
}
