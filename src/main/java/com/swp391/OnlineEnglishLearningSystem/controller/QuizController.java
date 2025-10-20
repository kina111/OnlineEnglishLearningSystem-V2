package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.AnswerOption;
import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.model.dto.AnsweredOption;
import com.swp391.OnlineEnglishLearningSystem.service.LessonService;
import com.swp391.OnlineEnglishLearningSystem.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    private final QuestionService questionService;
    private final LessonService lessonService;

    public QuizController(QuestionService questionService, LessonService lessonService) {
        this.questionService = questionService;
        this.lessonService = lessonService;
    }


    @GetMapping("/")
    public String getQuizPage() {
        return "redirect:/";
    }

    @GetMapping("/{lessonId}")
    public String getQuizPage(@PathVariable("lessonId") long lessonId, Model model, HttpSession session) {
        //Get a quiz by lesson id
        Lesson lesson = lessonService.findById(lessonId);
        if (lesson == null) {
            return "redirect:/";
        }
        //Session to store answers
        @SuppressWarnings("unchecked")
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute("quizAnswers");

        if (answers == null) {
            answers = new HashMap<>();
            session.setAttribute("quizAnswers", answers);
        }

        model.addAttribute("quizAnswers",answers);
        model.addAttribute("quiz", lesson);
        model.addAttribute("questionIndex",0);
        model.addAttribute("questionCount",lesson.getQuestions().size());
        return "redirect:/quiz/" + lessonId + "/0";
    }

    @GetMapping("/{quizId}/{questionIndex}")
    public String getQuizQuestionPage(Model model,
                                        @PathVariable("quizId") long quizId,
                                        @PathVariable("questionIndex") int questionIndex,
                                        HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute("quizAnswers");

        if (answers == null) {
            answers = new HashMap<>();
            session.setAttribute("quizAnswers", answers);
        }

        model.addAttribute("quizAnswers",answers);

        Lesson quiz = lessonService.findById(quizId);
        List<Question> questions = quiz.getQuestions();
        Question question = questions.get(questionIndex);
//        List<AnswerOption> answerOptions = question.getAnswerOptions();
//        question.setAnswerOptions(answerOptions);
        model.addAttribute("quiz", quiz);
        model.addAttribute("question", question);
        model.addAttribute("questionIndex", questionIndex);
        model.addAttribute("questionCount", quiz.getQuestions().size());
        return "quiz/showQuestion";
    }

    @PostMapping("/{quizId}/{questionIndex}")
    public String getQuizQuestionAnswerPage(Model model,
                                            @PathVariable("quizId") long quizId,
                                            @PathVariable("questionIndex") int questionIndex,
                                            @RequestParam(name = "answer", defaultValue = "",required = false) String answer,
                                            @RequestParam("action") String action,
                                            @RequestParam(name = "isBookmarked", defaultValue = "",required = false) String isBookmarked,
                                            HttpSession session) {
        Lesson quiz = lessonService.findById(quizId);
        List<Question> questions = quiz.getQuestions();
//        Question question = questions.get(questionIndex);

        // Save selected answer to session or service
//        quizService.saveAnswerProgress(quizId, questionId, answer, session);

        //Save answer to session
        @SuppressWarnings("unchecked")
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute("quizAnswers");
        if (answers == null) {
            answers = new HashMap<>();
            session.setAttribute("quizAnswers", answers);
        }
        if((answer!=null && !answer.isEmpty())||isBookmarked.equals("true")){
            AnsweredOption answeredOption = new AnsweredOption(answer, isBookmarked.equals("true"));
            answers.put(quiz.getQuestions().get(questionIndex).getId(), answeredOption);
        }

        model.addAttribute("quizAnswers",answers);
        //Navigate
        if ("previous".equals(action)) {
            return "redirect:/quiz/" + quizId + "/" + (questionIndex - 1);
        } else if ("next".equals(action)) {
            return "redirect:/quiz/" + quizId + "/" + (questionIndex + 1);
        } else if ("submit".equals(action)) {
//            quizService.finalizeQuiz(quizId, session); // e.g., save all and grade
            return "redirect:/quiz/" + quizId + "/result";
        }

        return "redirect:/quiz/" + quizId + "/question/" + questionIndex;
    }
}
