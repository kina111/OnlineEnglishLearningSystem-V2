package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.repository.QuestionRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.QuizAttemptRepository;
import com.swp391.OnlineEnglishLearningSystem.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizAttemptServiceImpl implements QuizAttemptService {
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepository questionRepository;

    public QuizAttemptServiceImpl(QuizAttemptRepository quizAttemptRepository, QuestionRepository questionRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.questionRepository = questionRepository;
    }

//    @Override
//    public QuizAttempt findQuizAttemptByQuizIdAndUserId(Lesson lesson, User user) {
//        return quizAttemptRepository.findByLessonAndUser(Lesson lesson, User user);
//    }
//
//
//    @Override
//    public List<QuizAttempt> findQuizAttemptByUserId(Long userId) {
//        return quizAttemptRepository.findByUserId(userId);
//    }

    @Override
    public List<QuizAttempt> findAllQuizAttempt() {
        return quizAttemptRepository.findAll();
    }

    @Override
    public QuizAttempt startQuizAttempt(User user, Lesson lesson) {
        // 1️⃣ Create attempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setLesson(lesson);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setEndTime(LocalDateTime.now().plusMinutes(lesson.getTimeLimitInMinutes()));

        // 2️⃣ Get all questions for this lesson
        List<Question> allQuestions = lesson.getQuestions();
        int numberOfQuestions = lesson.getNumberOfQuestions();
        Collections.shuffle(allQuestions);

        // 3️⃣ Limit random subset
        List<Question> selected = allQuestions.stream()
                .limit(numberOfQuestions)
                .collect(Collectors.toList());

        // 4️⃣ Map to attempt question
        int index = 0;
        for (Question q : selected) {
            QuizAttemptQuestion aq = new QuizAttemptQuestion();
            aq.setQuizAttempt(attempt);
            aq.setQuestion(q);
            aq.setQuestionOrder(index++);
            attempt.getQuestions().add(aq);
        }

        // 5️⃣ Save all
        return quizAttemptRepository.save(attempt);
    }

    @Override
    public QuizAttempt findQuizAttemptById(Long id) {
        return quizAttemptRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Quiz Attempt not found"));
    }

    @Override
    public boolean existsById(Long id) {
        return quizAttemptRepository.existsById(id);
    }

    @Override
    public List<Question> getQuestionsList(Long id) {
        List<Question> questions = new ArrayList<>();
        quizAttemptRepository.findById(id).ifPresent(quizAttempt -> {
            quizAttempt.getQuestions().forEach(quizAttemptQuestion -> {
                questions.add(quizAttemptQuestion.getQuestion());
            });
        });
        return questions;
    }
//
//    @Override
//    public void save(Long quizId, Long userId) {
//        quizAttemptRepository.
//    }

//    @Override
//    public void delete(Long quizId, Long userId) {
//        quizAttemptRepository
//    }

//    @Override
//    public boolean isQuizAttemptExist(Lesson lesson, User user) {
//        return quizAttemptRepository.findByUserIdAndQuizId(userId, quizId) != null;
//    }
}
