package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.model.QuizAttempt;
import com.swp391.OnlineEnglishLearningSystem.model.User;

import java.util.List;

public interface QuizAttemptService {
//    QuizAttempt findQuizAttemptByQuizIdAndUserId(Lesson lesson, User user);

//    List<QuizAttempt> findQuizAttemptByUserId(Long userId);
    List<QuizAttempt> findAllQuizAttempt();
    QuizAttempt startQuizAttempt(User user, Lesson lesson);
    QuizAttempt findQuizAttemptById(Long id);
    boolean existsById(Long id);
    void save(QuizAttempt quizAttempt);
    void delete(Long id);
//    boolean isQuizAttemptExist(Lesson lesson, User user);
    List<Question> getQuestionsList(Long id);
}
