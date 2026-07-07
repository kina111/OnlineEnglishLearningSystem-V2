package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.QuizAttemptQuestion;

import java.util.List;

public interface QuizAttemptQuestionService{
    List<QuizAttemptQuestion> findAll();
    QuizAttemptQuestion findById(Long quizAttemptId);
    void save(QuizAttemptQuestion quizAttemptQuestion);
    void delete(QuizAttemptQuestion quizAttemptQuestion);
//    void saveQuestion(Long quizAttemptId, AnsweredOption answeredOption);
//    void removeSelectedOption(Long attemptQuestionId, QuizAttemptSelectedOption attemptSelectedOption);

}
