package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.QuizAttemptQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptQuestionRepository extends JpaRepository <QuizAttemptQuestion, Long>{
//    QuizAttemptQuestion findByQuizAttemptIdAndQuestionId(Long quizAttemptId, Long questionId);
}
