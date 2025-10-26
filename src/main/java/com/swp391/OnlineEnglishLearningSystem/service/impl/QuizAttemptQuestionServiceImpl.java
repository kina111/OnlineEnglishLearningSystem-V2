package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.model.QuizAttemptQuestion;
import com.swp391.OnlineEnglishLearningSystem.model.QuizAttemptSelectedOption;
import com.swp391.OnlineEnglishLearningSystem.model.dto.AnsweredOption;
import com.swp391.OnlineEnglishLearningSystem.repository.QuizAttemptQuestionRepository;
import com.swp391.OnlineEnglishLearningSystem.service.QuizAttemptQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class QuizAttemptQuestionServiceImpl implements QuizAttemptQuestionService {
    @Autowired
    private QuizAttemptQuestionRepository quizAttemptQuestionRepository;
    public QuizAttemptQuestionServiceImpl(QuizAttemptQuestionRepository quizAttemptQuestionRepository) {
        this.quizAttemptQuestionRepository = quizAttemptQuestionRepository;
    }

    @Override
    public List<QuizAttemptQuestion> findAll() {
        return quizAttemptQuestionRepository.findAll();
    }

    @Override
    public QuizAttemptQuestion findById(Long quizAttemptId) {
        return quizAttemptQuestionRepository.findById(quizAttemptId).orElse(null);
    }

    @Override
    public void save(QuizAttemptQuestion quizAttemptQuestion) {
        quizAttemptQuestionRepository.save(quizAttemptQuestion);
    }

    @Override
    public void delete(QuizAttemptQuestion quizAttemptQuestion) {
        quizAttemptQuestionRepository.delete(quizAttemptQuestion);
    }

//    @Override
//    public void saveQuestion(Long quizAttemptId, AnsweredOption answeredOption) {
//        QuizAttemptQuestion quizAttemptQuestion = quizAttemptQuestionRepository.findById(quizAttemptId).orElse(null);
//        if (quizAttemptQuestion == null) {
//            return;
//        }
//        quizAttemptQuestion.setBookmarked(answeredOption.isBookmarked());
//        List<QuizAttemptSelectedOption> selectedOptions = quizAttemptQuestion.getSelectedOptions();
//        Question question = quizAttemptQuestion.getQuestion();
//        if (question.getQuestionType().equals("")) {
//
//        }
//
//
////        selectedOptions.forEach(selectedOption -> {})
//        quizAttemptQuestionRepository.save(quizAttemptQuestion);
//    }


}
