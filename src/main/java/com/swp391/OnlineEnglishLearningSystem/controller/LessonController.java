package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.model.dto.MultipleChoiceQuestionFormDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.ShortAnswerQuestionFormDTO;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LessonController {

    private final ChapterService chapterService;
    private final UploadService uploadService;
    private final LessonService lessonService;
    private final QuestionService questionService;
    private final AnswerOptionService answerOptionService;
    private final ShortAnswerOptionService shortAnswerOptionService;

    public LessonController(ChapterService chapterService, UploadService uploadService, LessonService lessonService, QuestionService questionService, AnswerOptionService answerOptionService, ShortAnswerOptionService shortAnswerOptionService) {
        this.chapterService = chapterService;
        this.uploadService = uploadService;
        this.lessonService = lessonService;
        this.questionService = questionService;
        this.answerOptionService = answerOptionService;
        this.shortAnswerOptionService = shortAnswerOptionService;
    }

    @PostMapping("/api/lessons/chapter/{chapterId}")
    @ResponseBody
    public ResponseEntity<?> createLesson(
            @PathVariable("chapterId") Long chapterId,
            @RequestParam("title") String title,
            @RequestParam("lessonType") String lessonType,
            @RequestParam(value = "estimatedTime", required = false) String estimatedTime,
            @RequestParam(value = "htmlContent", required = false) String htmlContent,
            @RequestParam(value = "video", required = false) MultipartFile videoFile,
            @RequestParam(value = "passRate", required = false) Integer passRate,
            @RequestParam(value = "timeLimitInMinutes", required = false) Integer timeLimitInMinutes){

        try{
            Chapter chapter = chapterService.findById(chapterId)
                    .orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
            Lesson newLecture = new Lesson();
            newLecture.setChapter(chapter);
            newLecture.setTitle(title);
            newLecture.setOrderNumber(chapter.getLessons().size() + 1);
            newLecture.setLessonType(Lesson.LessonType.valueOf(lessonType));

            switch (newLecture.getLessonType()){
                case LECTURE -> {
                    newLecture.setEstimatedTime(Integer.parseInt(estimatedTime));
                    newLecture.setHtmlContent(htmlContent);
                    if (videoFile != null && !videoFile.isEmpty()){
                        String videoUrl = uploadService.uploadVideo(videoFile, "lectures/videos");
                        newLecture.setVideoUrl(videoUrl);
                    }
                    break;
                }
                case QUIZ -> {
                    newLecture.setPassRate(passRate);
                    newLecture.setTimeLimitInMinutes(timeLimitInMinutes);
                    break;
                }
                default -> {
                    throw new IllegalArgumentException("Invalid lesson type");
                }
            }

            this.lessonService.save(newLecture);
            return new ResponseEntity<>(newLecture, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //==== Xử lí Quiz ====
    @GetMapping("/courses/{courseId}/quizzes/{quizId}/questions")
    public String getQuizQuestionBank(@PathVariable("quizId") Long id,
                                      @PathVariable("courseId") Long courseId,
                                      Model model){
        try{
            Lesson quiz = lessonService.findQuizAndQuestions(id);
            model.addAttribute("quiz", quiz);
            model.addAttribute("courseId", courseId);
            return "course/quizQuestions";
        }catch (Exception e){
            return "redirect:/courses/" + courseId;
        }
    }

    @GetMapping("/courses/{courseId}/quizzes/{quizId}/questions/multiple-choice/create")
    public String getMultipleChoiceQuestionPage(@PathVariable("courseId") Long courseId,
                                                @PathVariable("quizId") Long quizId,
                                                Model model){
        Lesson quiz = lessonService.findById(quizId);
        MultipleChoiceQuestionFormDTO multipleChoiceQuestionFormDTO = new MultipleChoiceQuestionFormDTO();

        multipleChoiceQuestionFormDTO.getAnswerOptions().add(new AnswerOption());
        multipleChoiceQuestionFormDTO.getAnswerOptions().add(new AnswerOption());

        model.addAttribute("mediaTypes", Question.MediaType.values());
        model.addAttribute("quiz", quiz);
        model.addAttribute("courseId", courseId);
        model.addAttribute("multipleChoiceQuestionFormDTO", multipleChoiceQuestionFormDTO);

        return "course/createMultipleChoiceQuestion";
    }

    @PostMapping("/courses/{courseId}/quizzes/{quizId}/questions/multiple-choice/create")
    public String createQuizQuestion(@PathVariable("courseId") Long courseId,
                                     @PathVariable("quizId") Long quizId,
                                     @Valid @ModelAttribute("multipleChoiceQuestionFormDTO") MultipleChoiceQuestionFormDTO multipleChoiceQuestionFormDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model){
        if (bindingResult.hasErrors()) {
            Lesson quiz = lessonService.findById(quizId);
            if (quiz == null) {
                throw new IllegalArgumentException("Không tìm thấy Quiz với ID: " + quizId);
            }
            model.addAttribute("courseId", courseId);
            model.addAttribute("quiz", quiz); // 2. Add quiz back to the model
            model.addAttribute("mediaTypes", Question.MediaType.values());// 3. Add mediaTypes back too

            return "course/createMultipleChoiceQuestion";
        }
        try{
            //save new question
            Question newQuestion = new Question();
            newQuestion.setQuestionType(Question.QuestionType.MULTIPLE_CHOICE);
            newQuestion.setLesson(lessonService.findById(quizId));
            newQuestion.setMediaType(multipleChoiceQuestionFormDTO.getMediaType());

            newQuestion.setContent(multipleChoiceQuestionFormDTO.getContent());
            if (multipleChoiceQuestionFormDTO.getMedia() != null && !multipleChoiceQuestionFormDTO.getMedia().isEmpty()){
                String fileName = this.uploadService.uploadFile(multipleChoiceQuestionFormDTO.getMedia(), "quizzes/media", null);
                newQuestion.setMediaUrl(fileName);
            }else{
                newQuestion.setMediaType(Question.MediaType.NONE);
            }
            this.questionService.save(newQuestion);

            //save AnswerOption
            List<AnswerOption> answerOptions = multipleChoiceQuestionFormDTO.getAnswerOptions();
            answerOptions.forEach(answerOption -> {
               answerOption.setQuestion(newQuestion);
               this.answerOptionService.save(answerOption);
            });

            redirectAttributes.addFlashAttribute("message", "Question created successfully");
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }catch (Exception e){
            bindingResult.reject("global.error", e.getMessage());
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }
    }

    @GetMapping("/courses/{courseId}/quizzes/{quizId}/questions/short-answer/create")
    public String getShortAnswerQuestionPage(@PathVariable("courseId") Long courseId,
                                             @PathVariable("quizId") Long quizId,
                                             Model model){
        Lesson quiz = lessonService.findById(quizId);
        ShortAnswerQuestionFormDTO newQuestion = new ShortAnswerQuestionFormDTO();

        model.addAttribute("courseId", courseId);
        model.addAttribute("mediaTypes", Question.MediaType.values());
        model.addAttribute("quiz", quiz);
        model.addAttribute("shortAnswerQuestionFormDTO", newQuestion);

        return "course/createShortAnswerQuestion";
    }

    @PostMapping("/courses/{courseId}/quizzes/{quizId}/questions/short-answer/create")
    public String createQuizQuestion(@PathVariable("courseId") Long courseId,
                                     @PathVariable("quizId") Long quizId,
                                     @Valid @ModelAttribute("shortAnswerQuestionFormDTO") ShortAnswerQuestionFormDTO shortAnswerQuestionFormDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model){
        if (bindingResult.hasErrors()) {
            Lesson quiz = lessonService.findById(quizId);
            if (quiz == null) {
                throw new IllegalArgumentException("Không tìm thấy Quiz với ID: " + quizId);
            }
            model.addAttribute("courseId", courseId);
            model.addAttribute("quiz", quiz); // 2. Add quiz back to the model
            model.addAttribute("mediaTypes", Question.MediaType.values());// 3. Add mediaTypes back too

            return "course/createShortAnswerQuestion";
        }
        try{
            //save new question
            Question newQuestion = new Question();
            newQuestion.setQuestionType(Question.QuestionType.SHORT_ANSWER);
            newQuestion.setLesson(lessonService.findById(quizId));
            newQuestion.setMediaType(shortAnswerQuestionFormDTO.getMediaType());
            newQuestion.setContent(shortAnswerQuestionFormDTO.getContent());

            if (shortAnswerQuestionFormDTO.getMedia() != null && !shortAnswerQuestionFormDTO.getMedia().isEmpty()){
                String fileName = this.uploadService.uploadImage(shortAnswerQuestionFormDTO.getMedia(), "quizzes/media");
                newQuestion.setMediaUrl(fileName);
            }else{
                newQuestion.setMediaType(Question.MediaType.NONE);
            }
            this.questionService.save(newQuestion);

            //save short answer option
            ShortAnswerOption answerOption = new ShortAnswerOption();
            answerOption.setQuestion(newQuestion);
            answerOption.setSolutionText(shortAnswerQuestionFormDTO.getSolutionText());
            this.shortAnswerOptionService.save(answerOption);

            redirectAttributes.addFlashAttribute("message", "Question created successfully");
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }catch (Exception e){
            bindingResult.reject("global.error", e.getMessage());
            return "redirect:/courses/" + courseId + "/quizzes/" + quizId + "/questions";
        }
    }
}
