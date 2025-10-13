package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.controller.ChapterController;
import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.repository.ChapterRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.CourseRepository;
import com.swp391.OnlineEnglishLearningSystem.service.ChapterService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChapterServiceImpl implements ChapterService {
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;

    public ChapterServiceImpl(CourseRepository courseRepository, ChapterRepository chapterRepository) {
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
    }

    @Override
    public Chapter createChapterForCourse(Long courseId, ChapterController.CreateChapterRequest createChapterRequest) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Chapter newChapter = new Chapter();
        int orderNumber = course.getChapters().size() + 1;

        newChapter.setName(createChapterRequest.getName());
        newChapter.setShortDescription(createChapterRequest.getShortDescription());
        newChapter.setOrderNumber(orderNumber);
        newChapter.setCourse(course);

        return this.chapterRepository.save(newChapter);
    }

    @Override
    public Optional<Chapter> findById(Long chapterId) {
        return Optional.ofNullable(chapterRepository.findById(chapterId).orElseThrow(() -> new IllegalArgumentException("Chapter not found")));
    }
}
