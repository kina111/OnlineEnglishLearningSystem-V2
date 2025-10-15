package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.ShortAnswerOption;
import com.swp391.OnlineEnglishLearningSystem.repository.ShortAnswerOptionRepository;
import com.swp391.OnlineEnglishLearningSystem.service.ShortAnswerOptionService;
import org.springframework.stereotype.Service;

@Service
public class ShortAnswerOptionServiceImpl implements ShortAnswerOptionService {
    private final ShortAnswerOptionRepository shortAnswerOptionRepository;

    public ShortAnswerOptionServiceImpl(ShortAnswerOptionRepository shortAnswerOptionRepository) {
        this.shortAnswerOptionRepository = shortAnswerOptionRepository;
    }


    @Override
    public void save(ShortAnswerOption answerOption) {
        this.shortAnswerOptionRepository.save(answerOption);
    }
}
