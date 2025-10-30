package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.AnswerOption;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.repository.AnswerOptionRepository;
import com.swp391.OnlineEnglishLearningSystem.service.AnswerOptionService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnswerOptionServiceImpl implements AnswerOptionService {
    private final AnswerOptionRepository answerOptionRepository;

    public AnswerOptionServiceImpl(AnswerOptionRepository answerOptionRepository) {
        this.answerOptionRepository = answerOptionRepository;
    }

    @Override
    public void save(AnswerOption answerOption) {
        this.answerOptionRepository.save(answerOption);
    }

    @Override
    public void deleteByQuestion(Question questionToUpdate) {
        List<AnswerOption> answerOptions = questionToUpdate.getAnswerOptions();
        this.answerOptionRepository.deleteAll(answerOptions);
    }

    @Override
    public AnswerOption findByAnswerOptionId(Long answerOptionId) {
        return answerOptionRepository.findById(answerOptionId).orElseThrow(() -> new IllegalArgumentException("Answer option not found"));
    }


}
