package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.AnswerOption;
import com.swp391.OnlineEnglishLearningSystem.model.Question;

public interface AnswerOptionService {
    void save(AnswerOption answerOption);

    void deleteByQuestion(Question questionToUpdate);

    AnswerOption findByAnswerOptionId(Long answerOptionId);

}
