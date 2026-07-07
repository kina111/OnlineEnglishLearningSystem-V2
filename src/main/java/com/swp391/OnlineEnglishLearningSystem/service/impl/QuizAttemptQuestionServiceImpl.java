package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.QuizAttemptQuestion;
import com.swp391.OnlineEnglishLearningSystem.repository.QuizAttemptQuestionRepository;
import com.swp391.OnlineEnglishLearningSystem.service.QuizAttemptQuestionService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizAttemptQuestionServiceImpl implements QuizAttemptQuestionService {
    private final QuizAttemptQuestionRepository quizAttemptQuestionRepository;

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
