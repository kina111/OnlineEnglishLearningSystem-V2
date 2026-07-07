package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.controller.ChapterController;
import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.repository.ChapterRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.CourseRepository;
import com.swp391.OnlineEnglishLearningSystem.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public Chapter createChapterForCourse(Long courseId, ChapterController.CreateChapterRequest createChapterRequest) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

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
        return Optional.ofNullable(chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found")));
    }

    @Override
    public void deleteById(Long chapterId) {
        this.chapterRepository.deleteById(chapterId);
    }

    @Override
    @Transactional
    public void deleteChapterAndReorder(Long courseId, Long chapterId) {
        // 1. Kiểm tra sự tồn tại (quan trọng!)
        if (!chapterRepository.existsById(chapterId)) {
            throw new NoSuchElementException("Không tìm thấy Chapter với id: " + chapterId);
        }

        // 2. Xóa chapter
        chapterRepository.deleteById(chapterId);

        // 3. Cập nhật lại thứ tự (logic này nên nằm trong service hoặc repository)
        List<Chapter> remainingChapters = chapterRepository.findByCourseIdOrderByOrderNumberAsc(courseId);
        for (int i = 0; i < remainingChapters.size(); i++) {
            Chapter chapter = remainingChapters.get(i);
            chapter.setOrderNumber(i + 1); // Cập nhật lại order number thành 1, 2, 3...
            chapterRepository.save(chapter);
        }
    }

    @Override
    public Chapter updateChapter(Long chapterId, ChapterController.CreateChapterRequest chapter) {
        Chapter updateChapter = this.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
        updateChapter.setName(chapter.getName());
        updateChapter.setShortDescription(chapter.getShortDescription());
        return this.chapterRepository.save(updateChapter);
    }
}
