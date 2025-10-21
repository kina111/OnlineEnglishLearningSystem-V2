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

    private static final String QUIZ_SESSION = "quizAnswers";
    private static final String QUIZ_PATH = "redirect:/quiz/";
    
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
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute(QUIZ_SESSION);

        if (answers == null) {
            answers = new HashMap<>();
            session.setAttribute(QUIZ_SESSION, answers);
        }

        model.addAttribute(QUIZ_SESSION,answers);
        model.addAttribute("quiz", lesson);
        model.addAttribute("questionIndex",0);
        model.addAttribute("questionCount",lesson.getQuestions().size());
        return QUIZ_PATH + lessonId + "/0";
    }
    //Get question page
    @GetMapping("/{quizId}/{questionIndex}")
    public String getQuizQuestionPage(Model model,
                                        @PathVariable("quizId") long quizId,
                                        @PathVariable("questionIndex") int questionIndex,
                                        HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute(QUIZ_SESSION);

        if (answers == null) {
            answers = new HashMap<>();
            session.setAttribute(QUIZ_SESSION, answers);
        }
        //?
        model.addAttribute(QUIZ_SESSION,answers);
//        model.addAttribute("answeredOption",quizId);

        Lesson quiz = lessonService.findById(quizId);
        List<Question> questions = quiz.getQuestions();
        Question question = questions.get(questionIndex);
//        List<AnswerOption> answerOptions = question.getAnswerOptions();
//        question.setAnswerOptions(answerOptions);
        //put answer option to
        long answerOptionId = 0;
        if(answers.containsKey(question.getId())){
            try{
                answerOptionId = Long.parseLong(answers.get(question.getId()).getAnswerId());
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }

        if(answers.containsKey(question.getId())){
            model.addAttribute("answeredOption",answerOptionId);
        }
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
                                            @RequestParam(name = "isBookmarked",defaultValue = "false",required = false) Boolean isBookmarked,
                                            HttpSession session) {
        Lesson quiz = lessonService.findById(quizId);
        List<Question> questions = quiz.getQuestions();
//        Question question = questions.get(questionIndex);

        // Save selected answer to session or service
//        quizService.saveAnswerProgress(quizId, questionId, answer, session);

        //Save answer to session
        @SuppressWarnings("unchecked")
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute(QUIZ_SESSION);
        if (answers == null) {
            answers = new HashMap<>();
            session.setAttribute(QUIZ_SESSION, answers);
        }
        if((answer!=null && !answer.isEmpty())||isBookmarked){
            AnsweredOption answeredOption = new AnsweredOption(answer, isBookmarked);
            answers.put(quiz.getQuestions().get(questionIndex).getId(), answeredOption);
        }

        model.addAttribute(QUIZ_SESSION,answers);
        //Navigate
        if ("previous".equals(action)) {
            return QUIZ_PATH + quizId + "/" + (questionIndex - 1);
        } else if ("next".equals(action)) {
            return QUIZ_PATH + quizId + "/" + (questionIndex + 1);
        } else if ("submit".equals(action)) {
//            quizService.finalizeQuiz(quizId, session); // e.g., save all and grade
            return QUIZ_PATH + quizId + "/result";
        }

        return QUIZ_PATH + quizId + "/question/" + questionIndex;
    }
}
