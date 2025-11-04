package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.model.dto.AnsweredOption;
import com.swp391.OnlineEnglishLearningSystem.model.dto.QuestionView;
import com.swp391.OnlineEnglishLearningSystem.repository.AnswerOptionRepository;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final AnswerOptionService answerOptionService;
    private final UserService userService;
    private final QuizAttemptService quizAttemptService;
    private final UserLessonService userLessonService;

    public QuizController(QuestionService questionService, LessonService lessonService,
                          AnswerOptionService answerOptionService, UserService userService,
                          QuizAttemptService quizAttemptService, UserLessonService userLessonService) {
        this.questionService = questionService;
        this.lessonService = lessonService;
        this.answerOptionService = answerOptionService;
        this.userService = userService;
        this.quizAttemptService = quizAttemptService;
        this.userLessonService = userLessonService;
    }


    @GetMapping("/")
    public String getQuizPage() {
        return "redirect:/";
    }

    @GetMapping("/{lessonId}/start")
    public String getQuizPage(@PathVariable("lessonId") long lessonId, Model model, HttpSession session) {
        //Temp hardcode user------------------------------
        Long userId = (Long) session.getAttribute("currentUserId");
        if(userId == null){
            return "redirect:/";
        }
        User user = userService.getUserById(userId);
        //Get a quiz by lesson id
        Lesson lesson = lessonService.findById(lessonId);
        if (lesson == null||user == null) {
            return "redirect:/";
        }
        //Start quizAttempt
        QuizAttempt quizAttempt = quizAttemptService.startQuizAttempt(user, lesson);
        //Session to store answers
        @SuppressWarnings("unchecked")
        Map<Long, AnsweredOption> answers = new HashMap<>();
        session.setAttribute(QUIZ_SESSION, answers);
        session.setAttribute("quizAttemptId", quizAttempt.getId());

        model.addAttribute(QUIZ_SESSION,answers);
        //---check this
//        model.addAttribute("quiz", quizAttempt.getLesson());
        model.addAttribute("questionIndex",0);
        model.addAttribute("questionCount",quizAttempt.getQuestions().size());
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
        Long quizAttemptId = (Long) session.getAttribute("quizAttemptId");
        if(quizAttemptId == null|| !quizAttemptService.existsById(quizAttemptId)){
            return QUIZ_PATH + quizId + "/start";
        }
        if (answers == null) {
            answers = new HashMap<>();
            session.setAttribute(QUIZ_SESSION, answers);
        }
        QuizAttempt quizAttempt = quizAttemptService.findQuizAttemptById(quizAttemptId);
        List<QuizAttemptQuestion> questions = quizAttempt.getQuestions();

        Question question = questions.get(questionIndex).getQuestion();

        //put answer option to
        List<Long> answerOptionId = new ArrayList<>();
        if(answers.containsKey(question.getId())&&!answers.get(question.getId()).getAnswer().isEmpty()){
            try{

                for(String answer : answers.get(question.getId()).getAnswer()){
                    answerOptionId.add(Long.parseLong(answer));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if(answers.containsKey(question.getId())){
            model.addAttribute("answeredOptionId",answerOptionId);
            model.addAttribute("answeredOption",answers.get(question.getId()));
        }
        String questionType = "SHORT_ANSWER";
        if (question.getQuestionType() == Question.QuestionType.MULTIPLE_CHOICE){
            questionType = "MULTIPLE_CHOICE";
        }


        model.addAttribute("endTime", quizAttempt.getEndTime());
        model.addAttribute("progress",answers);
        model.addAttribute("question", question);
        model.addAttribute("questionType", questionType);
        model.addAttribute("questionIndex", questionIndex);
        model.addAttribute("questionCount", quizAttempt.getQuestions().size());
        return "quiz/showQuestion";
    }

    //Load question answer page
    @PostMapping("/{quizId}/{questionIndex}")
    public String getQuizQuestionAnswerPage(Model model,
                                            @PathVariable("quizId") long quizId,
                                            @PathVariable("questionIndex") int questionIndex,
                                            @RequestParam(name = "answer",required = false) List<String> answer,
                                            @RequestParam(value = "action",required = false) String action,
                                            @RequestParam(name = "isMarked",defaultValue = "false",required = false) Boolean isBookmarked,
                                            @RequestParam(required = false, name = "autoAction") String autoAction,
                                            HttpSession session) {

        Long quizAttemptId = (Long) session.getAttribute("quizAttemptId");
        if(quizAttemptId == null|| !quizAttemptService.existsById(quizAttemptId)){
            return QUIZ_PATH + quizId + "/start";
        }

        QuizAttempt quizAttempt = quizAttemptService.findQuizAttemptById(quizAttemptId);
//        List<QuizAttemptQuestion> questions = quizAttempt.getQuestions();
//        Question question = questions.get(questionIndex);



        //Save answer to session
        @SuppressWarnings("unchecked")
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute(QUIZ_SESSION);
        if (answers == null) {
            answers = new HashMap<>();
            session.setAttribute(QUIZ_SESSION, answers);
        }
        if((answer!=null && !answer.isEmpty())||isBookmarked){
            AnsweredOption answeredOption = new AnsweredOption(answer, isBookmarked);
            answers.put(quizAttempt.getQuestions().get(questionIndex).getQuestion().getId(), answeredOption);
        }


        //Navigate
        action = (autoAction != null && !autoAction.isEmpty()) ? autoAction : action.toLowerCase();
        if ("previous".equals(action)) {
            return QUIZ_PATH + quizId + "/" + (questionIndex - 1);
        } else if ("next".equals(action)) {
            return QUIZ_PATH + quizId + "/" + (questionIndex + 1);
        } else if ("finish".equals(action)) {
//            quizService.finalizeQuiz(quizId, session); // e.g., save all and grade
            return QUIZ_PATH + quizId + "/result";
        } else if ("progress".equals(action)) {
            return QUIZ_PATH + quizId + "/progress";
        }
        //show bug
        return QUIZ_PATH + quizId + "/" + questionIndex;
    }

    @GetMapping("/{quizId}/result")
    public String getQuizResult(Model model,HttpSession session,@PathVariable("quizId") long quizId) {
        Lesson lesson = lessonService.findById(quizId);
        List<Question> questions = lesson.getQuestions();

        @SuppressWarnings("unchecked")
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute(QUIZ_SESSION);
        Long quizAttemptId = (Long) session.getAttribute("quizAttemptId");
        if(quizAttemptId == null|| !quizAttemptService.existsById(quizAttemptId)){
            return QUIZ_PATH + quizId + "/start";
        }
        QuizAttempt quizAttempt = quizAttemptService.findQuizAttemptById(quizAttemptId);
        if(quizAttempt.getEndTime() == null){
            return QUIZ_PATH + quizId + "/start";
        }
        quizAttemptService.finishQuizAttempt(quizAttempt,answers);
        if(quizAttempt.getPassed()){
            userLessonService.updateIsCompleted(quizAttempt.getUser().getId(), quizId);
        }
        session.removeAttribute(QUIZ_SESSION);
        session.removeAttribute("quizAttemptId");
        return QUIZ_PATH+"attempt/"+quizAttemptId+"/result";
    }


    @GetMapping("/attempt/{quizAttemptId}/result")
    public String showQuizResult(@PathVariable Long quizAttemptId, Model model,HttpSession session) {
        QuizAttempt quizAttempt = quizAttemptService.findQuizAttemptById(quizAttemptId);
        Long enrollmentId = (Long) session.getAttribute("currentEnrollmentId");
        int totalQuestions = quizAttempt.getQuestions().size();
        int correctAnswers = (int) quizAttempt.getQuestions().stream()
                .filter(QuizAttemptQuestion::getCorrect)
                .count();

        // Độ chính xác (score làm tròn xuống theo yêu cầu)
        int accuracy = (int) Math.floor((double) correctAnswers * 100 / totalQuestions);

        // Tính thời gian làm bài (tính phút và giây)
        Duration duration = Duration.between(quizAttempt.getStartTime(), quizAttempt.getCompletedTime());
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds() % 60;

        model.addAttribute("quizAttempt", quizAttempt);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("accuracy", accuracy);
        model.addAttribute("minutes", minutes);
        model.addAttribute("seconds", seconds);
        model.addAttribute("currentEnrollmentId", enrollmentId);
        return "quiz/quiz_result";
    }

    //Get quiz progress page
//    @GetMapping("/{quizId}/progress")
//    public String getQuizProgress(Model model,HttpSession session,@PathVariable("quizId") long quizId) {
//        Lesson lesson = lessonService.findById(quizId);
//        List<Question> questions = lesson.getQuestions();
//        @SuppressWarnings("unchecked")
//        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute(QUIZ_SESSION);
//
//        List<QuestionView> questionViews = new ArrayList<>();
//        for (Question q : questions) {
//            AnsweredOption option = answers.get(q.getId());
//            questionViews.add(new QuestionView(q, (option != null &&!option.getAnswerId().isEmpty()), (option != null && option.isBookmarked())));
//        }
//        model.addAttribute("questionViews", questionViews);
//
//
//        model.addAttribute("questions",questions);
//        model.addAttribute("answers",answers);
//        model.addAttribute("quiz",lesson);
//
//        return "quiz/progress";
//    }
    @GetMapping("/{quizId}/progress")
    public String showProgressPage(@PathVariable Long quizId, Model model, HttpSession session) {
//        Lesson lesson = lessonService.findById(quizId);
//        List<Question> questions = lesson.getQuestions();
        Long quizAttemptId = (Long) session.getAttribute("quizAttemptId");
        if(quizAttemptId == null|| !quizAttemptService.existsById(quizAttemptId)){
            return QUIZ_PATH + quizId + "/start";
        }
        QuizAttempt quizAttempt = quizAttemptService.findQuizAttemptById(quizAttemptId);
        List<Question> questions = quizAttemptService.getQuestionsList(quizAttemptId);


        model.addAttribute("quizId", quizId);
        model.addAttribute("questions", questions);
        model.addAttribute("endTime", quizAttempt.getEndTime());
        Map<Long, AnsweredOption> answers = (Map<Long, AnsweredOption>) session.getAttribute(QUIZ_SESSION);
        model.addAttribute("answers", answers != null ? answers : new HashMap<>());
        return "/quiz/quiz-progress";
    }

    @GetMapping("/attempt/{attemptId}/review")
    public String getQuizReviewPage( @PathVariable Long attemptId, Model model,HttpSession session) {
        QuizAttempt quizAttempt = quizAttemptService.findQuizAttemptById(attemptId);
        Long enrollmentId = session.getAttribute("currentEnrollmentId") != null ? (Long) session.getAttribute("enrollmentId") : null;

        int totalQuestions = quizAttempt.getQuestions().size();
        int correctAnswers = (int) quizAttempt.getQuestions().stream()
                .filter(QuizAttemptQuestion::getCorrect)
                .count();
        int accuracy = (int) Math.floor((double) correctAnswers * 100 / totalQuestions);

        model.addAttribute("quizAttempt", quizAttempt);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("accuracy", accuracy);
        model.addAttribute("currentEnrollmentId", enrollmentId);
        return "quiz/attempt_detail";
    }

}
