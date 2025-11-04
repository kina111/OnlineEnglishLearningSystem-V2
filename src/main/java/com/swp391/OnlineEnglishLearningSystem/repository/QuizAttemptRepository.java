package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository <QuizAttempt, Long>{
//    QuizAttempt findByUserIdAndQuizId(Long userId, Long quizId);
//    List<QuizAttempt> findByUserId(Long userId);
}
