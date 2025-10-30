package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import com.swp391.OnlineEnglishLearningSystem.model.Lesson;
import com.swp391.OnlineEnglishLearningSystem.model.dto.AnsweredOption;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CreateLectureRequest;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CreateQuizRequest;
import com.swp391.OnlineEnglishLearningSystem.repository.ChapterRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.LessonRepository;
import com.swp391.OnlineEnglishLearningSystem.service.LessonService;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import com.swp391.OnlineEnglishLearningSystem.util.VideoDuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final UploadService uploadService;

    public LessonServiceImpl(LessonRepository lessonRepository, ChapterRepository chapterRepository, UploadService uploadService) {
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.uploadService = uploadService;
    }

    @Override
    public void save(Lesson newLecture) {
        this.lessonRepository.save(newLecture);
    }

    @Override
    public Lesson findQuizAndQuestions(Long quizId) {
        return this.lessonRepository.findQuizWithQuestions(quizId).orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
    }

    @Override
    public Lesson findById(Long quizId) {
        return this.lessonRepository.findById(quizId).orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
    }

    @Override
    @Transactional
    public Lesson createLecture(Long chapterId, CreateLectureRequest request) {
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
        Lesson lecture = new Lesson();
        lecture.setChapter(chapter);
        lecture.setLessonType(Lesson.LessonType.LECTURE);
        lecture.setTitle(request.getTitle());
        lecture.setOrderNumber(chapter.getLessons().size() + 1);

        lecture.setEstimatedTime(request.getEstimatedTime());
        lecture.setHtmlContent(request.getHtmlContent());
        try{
            String videoUrl = uploadService.uploadVideo(request.getVideo(), "lectures/videos");
            long duration = VideoDuration.getVideoDuration(request.getVideo());
            lecture.setVideoUrl(videoUrl);
            lecture.setDuration(duration);
        }catch (Exception e){
            throw new IllegalArgumentException("Video file is error!");
        }
        return this.lessonRepository.save(lecture);
    }

    @Transactional
    @Override
    public Lesson createQuiz(Long chapterId, CreateQuizRequest request) {
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
        Lesson quiz = new Lesson();
        quiz.setChapter(chapter);
        quiz.setLessonType(Lesson.LessonType.QUIZ);
        quiz.setTitle(request.getTitle());
        quiz.setOrderNumber(chapter.getLessons().size() + 1);

        quiz.setPassRate(request.getPassRate());
        quiz.setNumberOfQuestions(request.getNumberOfQuestions());
        quiz.setTimeLimitInMinutes(request.getTimeLimitInMinutes());

        return this.lessonRepository.save(quiz);
    }

    @Override
    public Lesson updateLectureById(Long lectureId, CreateLectureRequest request) {
        Lesson lectureToUpdate = this.lessonRepository.findById(lectureId).orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        lectureToUpdate.setTitle(request.getTitle());
        lectureToUpdate.setEstimatedTime(request.getEstimatedTime());
        lectureToUpdate.setHtmlContent(request.getHtmlContent());
        try{
            String videoUrl = uploadService.uploadVideo(request.getVideo(), "lectures/videos");
            long duration = VideoDuration.getVideoDuration(request.getVideo());
            lectureToUpdate.setVideoUrl(videoUrl);
            lectureToUpdate.setDuration(duration);
        }catch (Exception e){
            throw new IllegalArgumentException("Video file is error!");
        }
        return this.lessonRepository.save(lectureToUpdate);
    }

    @Override
    public Lesson updateQuizById(Long quizId, CreateQuizRequest request) {
        Lesson quizToUpdate = this.lessonRepository.findById(quizId).orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        quizToUpdate.setTitle(request.getTitle());
        quizToUpdate.setPassRate(request.getPassRate());
        quizToUpdate.setNumberOfQuestions(request.getNumberOfQuestions());
        quizToUpdate.setTimeLimitInMinutes(request.getTimeLimitInMinutes());

        return this.lessonRepository.save(quizToUpdate);
    }

    @Override
    public void deleteAndReorder(Long chapterId, Long lessonId) {
        Chapter chapter = this.chapterRepository.findById(chapterId).orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
        Lesson lessonToDelete = this.lessonRepository.findById(lessonId).orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        if (lessonToDelete.getChapter() != chapter) {
            throw new IllegalArgumentException("Lesson does not belong to the chapter");
        }
        this.lessonRepository.delete(lessonToDelete);

        //cập nhật lại orderNumber cho các lesson còn lại
        List<Lesson> remainingLessons = this.lessonRepository.findByChapterIdOrderByOrderNumberAsc(chapterId);
        for (int i = 0; i < remainingLessons.size(); i++) {
            Lesson lesson = remainingLessons.get(i);
            lesson.setOrderNumber(i + 1);
            this.lessonRepository.save(lesson);
        }
    }

}
