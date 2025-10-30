package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CreateLectureRequest;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CreateQuizRequest;
import jakarta.validation.Valid;

public interface LessonService {
    void save(Lesson newLecture);

    Lesson findQuizAndQuestions(Long quizId);

    Lesson findById(Long quizId);

    Lesson createLecture(Long chapterId, @Valid CreateLectureRequest request);

    Lesson createQuiz(Long chapterId, @Valid CreateQuizRequest request);

    Lesson updateLectureById(Long lectureId, @Valid CreateLectureRequest request);

    Lesson updateQuizById(Long quizId, @Valid CreateQuizRequest request);

    void deleteAndReorder(Long chapterId, Long lessonId);

}
