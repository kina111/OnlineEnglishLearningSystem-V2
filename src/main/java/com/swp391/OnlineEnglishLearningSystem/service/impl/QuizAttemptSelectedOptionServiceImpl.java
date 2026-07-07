package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.QuizAttemptSelectedOption;
import com.swp391.OnlineEnglishLearningSystem.repository.QuizAttemptSelectedOptionRepository;
import com.swp391.OnlineEnglishLearningSystem.service.QuizAttemptSelectedOptionService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizAttemptSelectedOptionServiceImpl implements QuizAttemptSelectedOptionService {
    private final QuizAttemptSelectedOptionRepository quizAttemptSelectedOptionRepository;

    @Override
    public void save(QuizAttemptSelectedOption quizAttemptSelectedOption) {
        quizAttemptSelectedOptionRepository.save(quizAttemptSelectedOption);
    }

    @Override
    public void delete(Long attemptOptionId) {
        quizAttemptSelectedOptionRepository.deleteById(attemptOptionId);
    }

    @Override
    public QuizAttemptSelectedOption findByAttemptOptionId(Long attemptOptionId) {
        return quizAttemptSelectedOptionRepository.findById(attemptOptionId).orElse(null);
    }

    @Override
    public List<QuizAttemptSelectedOption> findAllByQuizAttemptId(Long quizAttemptId) {
        return quizAttemptSelectedOptionRepository.findAll();
    }
}
