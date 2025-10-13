package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.Question;
import com.swp391.OnlineEnglishLearningSystem.service.ChapterService;
import com.swp391.OnlineEnglishLearningSystem.service.LessonService;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class LessonController {

    private final ChapterService chapterService;
    private final UploadService uploadService;
    private final LessonService lessonService;


    public LessonController(ChapterService chapterService, UploadService uploadService, LessonService lessonService) {
        this.chapterService = chapterService;
        this.uploadService = uploadService;
        this.lessonService = lessonService;
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
    @GetMapping("/quizzes/{quizId}/questions")
    public String getQuizQuestionsPage(@PathVariable("quizId") Long quizId, Model model){
        Lesson quiz = lessonService.findQuizAndQuestions(quizId);

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quiz.getQuestions());

        model.addAttribute("newQuestion", new Question());

        return "course/quizQuestions";
    }
}
