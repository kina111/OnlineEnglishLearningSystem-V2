package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.dto.ChapterLearningDTO;

import java.util.List;

public interface LearningService {
    List<ChapterLearningDTO> prepareLearningViewData(long userId, long enrollmentId);
}
