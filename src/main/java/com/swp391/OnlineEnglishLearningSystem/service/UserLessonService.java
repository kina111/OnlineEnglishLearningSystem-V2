package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserLessonLearningDTO;

import java.util.List;

public interface UserLessonService {
    void createFullUserLesson(Enrollment enrollment);

    void updateIsCompleted(long userId, long lessonId);

    boolean existsByLessonIdAndUserId(long lessonId, long userId);
}
