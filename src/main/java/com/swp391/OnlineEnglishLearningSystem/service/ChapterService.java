package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.controller.ChapterController;
import com.swp391.OnlineEnglishLearningSystem.model.Chapter;

public interface ChapterService {
    Chapter createChapterForCourse(Long courseId, ChapterController.CreateChapterRequest createChapterRequest);
}
