package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.AnswerOption;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.model.QuizAttempt;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class QuizEvaluationService {
//    @Transactional
//    public double evaluateQuizAttempt(QuizAttempt attempt) {
//        int correctCount = 0;
//        int total = attempt.getAttemptAnswers().size();
//
//        for (AttemptAnswer ans : attempt.getAttemptAnswers()) {
//            Question q = ans.getQuestion();
//
//            if (q.getQuestionType() == Question.QuestionType.MULTIPLE_CHOICE) {
//                boolean isCorrect = q.getAnswerOptions().stream()
//                        .filter(AnswerOption::isCorrect)
//                        .map(AnswerOption::getText)
//                        .anyMatch(correctText -> correctText.equalsIgnoreCase(ans.getUserAnswer()));
//
//                ans.setCorrect(isCorrect);
//                if (isCorrect) correctCount++;
//
//            } else if (q.getQuestionType() == Question.QuestionType.SHORT_ANSWER) {
//                String correctAnswer = q.getShortAnswerOption().getCorrectAnswer();
//                boolean isCorrect = correctAnswer.trim().equalsIgnoreCase(ans.getUserAnswer().trim());
//                ans.setCorrect(isCorrect);
//                if (isCorrect) correctCount++;
//            }
//        }
//
//        double score = (double) correctCount / total * 100;
//        attempt.setScore(score);
//        attempt.setPassed(score >= attempt.getQuiz().getPassingScore()); // optional
//        attempt.setCompletedAt(LocalDateTime.now());
//
//        return score;
//    }
}
