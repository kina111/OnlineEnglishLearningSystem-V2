package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.QuizAttemptSelectedOption;

import java.util.List;

public interface QuizAttemptSelectedOptionService {
    void save(QuizAttemptSelectedOption quizAttemptSelectedOption);
    void delete(Long attemptOptionId);
    QuizAttemptSelectedOption findByAttemptOptionId(Long attemptOptionId);
    List<QuizAttemptSelectedOption> findAllByQuizAttemptId(Long quizAttemptId);

}
