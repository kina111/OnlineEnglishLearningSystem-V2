package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;

public interface LessonService {
    void save(Lesson newLecture);

    Lesson findQuizAndQuestions(Long quizId);

    Lesson findById(Long quizId);
}
