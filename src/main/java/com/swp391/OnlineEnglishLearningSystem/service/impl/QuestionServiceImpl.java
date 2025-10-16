package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.repository.QuestionRepository;
import com.swp391.OnlineEnglishLearningSystem.service.QuestionService;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void save(Question newQuestion) {
        this.questionRepository.save(newQuestion);
    }

    @Override
    public Question findById(Long questionId) {
        return this.questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question not found"));
    }

    @Override
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    @Override
    public Question findByIdWithAnswerOptions(Long questionId) {
        return this.questionRepository.findByIdWithAnswerOptions(questionId).orElseThrow(() -> new IllegalArgumentException("Question not found"));
    }
}
