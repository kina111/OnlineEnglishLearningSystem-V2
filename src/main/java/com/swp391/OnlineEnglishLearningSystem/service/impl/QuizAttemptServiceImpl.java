package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.model.dto.AnsweredOption;
import com.swp391.OnlineEnglishLearningSystem.repository.QuestionRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.QuizAttemptRepository;
import com.swp391.OnlineEnglishLearningSystem.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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
        int index = 1;
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
    public void save(QuizAttempt quizAttempt) {
        quizAttemptRepository.save(quizAttempt);
    }

    @Override
    public void delete(Long id) {
        quizAttemptRepository.deleteById(id);
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

    @Override
    public QuizAttempt finishQuizAttempt(QuizAttempt attempt, Map<Long, AnsweredOption> answersMap) {
        double totalScore = 0;
        int totalQuestions = attempt.getQuestions().size();

        for (QuizAttemptQuestion aq : attempt.getQuestions()) {
            Long questionId = aq.getQuestion().getId();
            AnsweredOption userAnswer = answersMap.get(questionId);
            if (userAnswer == null) continue;

            aq.setBookmarked(userAnswer.isBookmarked());
            boolean isCorrect = false;

            Question question = aq.getQuestion();
            if (question.getQuestionType() == Question.QuestionType.MULTIPLE_CHOICE) {
                // handle multiple choice (multiple correct answers)
                List<AnswerOption> correctOptions = question.getAnswerOptions().stream()
                        .filter(AnswerOption::getCorrect)
                        .toList();

                List<Long> correctIds = correctOptions.stream()
                        .map(AnswerOption::getId)
                        .toList();

                // user’s selected answers (stored as String IDs)
                List<Long> selectedIds = userAnswer.getAnswer()
                        .stream()
                        .map(Long::parseLong)
                        .toList();

                // save user selected options
                for (Long selectedId : selectedIds) {
                    AnswerOption selectedOption = question.getAnswerOptions().stream()
                            .filter(opt -> opt.getId().equals(selectedId))
                            .findFirst()
                            .orElse(null);

                    QuizAttemptSelectedOption sao = new QuizAttemptSelectedOption();
                    sao.setAttemptQuestion(aq);
                    sao.setOption(selectedOption);
                    aq.getSelectedOptions().add(sao);
                }

                // correctness check — compare sets of IDs
                isCorrect = new HashSet<>(selectedIds).equals(new HashSet<>(correctIds));
            }
            else if (question.getQuestionType() == Question.QuestionType.SHORT_ANSWER) {
                String userVal = userAnswer.getAnswer().isEmpty() ? "" : userAnswer.getAnswer().get(0).trim().toLowerCase();

                QuizAttemptSelectedOption sao = new QuizAttemptSelectedOption();
                sao.setAttemptQuestion(aq);
                sao.setSelectedValue(userVal);
                aq.getSelectedOptions().add(sao);
                if(question.getShortAnswerOption()!=null) {
                    String correct = question.getShortAnswerOption().getSolutionText().trim().toLowerCase();
                    isCorrect = userVal.equalsIgnoreCase(correct);
                }
            }

            aq.setCorrect(isCorrect);
            if (isCorrect) totalScore++;
        }

        double finalScore = (totalScore / totalQuestions) * 100;
        attempt.setScore(finalScore);
        attempt.setPassed(finalScore >= attempt.getLesson().getPassRate());
        attempt.setCompletedTime(LocalDateTime.now());
//        attempt.setTimeTaken(Duration.between(attempt.getStartedAt(), attempt.getFinishedAt()).toSeconds());

        return quizAttemptRepository.save(attempt);
    }
}
