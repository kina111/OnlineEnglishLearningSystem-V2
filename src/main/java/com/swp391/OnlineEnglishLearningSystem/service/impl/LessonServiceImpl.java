package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.repository.LessonRepository;
import com.swp391.OnlineEnglishLearningSystem.service.LessonService;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void save(Lesson newLecture) {
        this.lessonRepository.save(newLecture);
    }

    @Override
    public Lesson findQuizAndQuestions(Long quizId) {
        return this.lessonRepository.findQuizWithQuestions(quizId).orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
    }
}
