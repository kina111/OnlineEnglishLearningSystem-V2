package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.model.dto.ChapterLearningDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserLessonLearningDTO;
import com.swp391.OnlineEnglishLearningSystem.repository.EnrollmentRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserLessonRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserRepository;
import com.swp391.OnlineEnglishLearningSystem.service.LearningService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserLessonRepository userLessonRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ChapterLearningDTO> prepareLearningViewData(long userId, long enrollmentId) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // 1. Lấy cấu trúc chính (Enrollment -> Course -> Chapters -> Lessons)
        Enrollment enrollment = enrollmentRepository.findByIdAndUserIdWithCourse(enrollmentId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found!")); // Ném exception cụ thể

        Course course = enrollment.getCourse();
        List<Chapter> chapters = course.getChapters();

        // 2. Lấy tất cả tiến độ học của user cho enrollment này
        List<UserLesson> userProgressList = userLessonRepository.findAllByEnrollmentIdAndUserIdWithLesson(enrollmentId, userId);

        // 3. Tạo Map để tra cứu tiến độ nhanh chóng (Key: lessonId, Value: UserLesson)
        Map<Long, UserLesson> progressMap = userProgressList.stream()
                .collect(Collectors.toMap(ul -> ul.getLesson().getId(), Function.identity()));

        // 4. Xây dựng cấu trúc DTO trả về
        List<ChapterLearningDTO> chapterLearningDTOs = new ArrayList<>();
        if (chapters != null) {
            // Sắp xếp chapter theo orderNumber nếu cần
            chapters.sort(Comparator.comparingInt(Chapter::getOrderNumber));

            for (Chapter chapter : chapters) {
                List<UserLessonLearningDTO> lessonDTOs = new ArrayList<>();
                List<Lesson> lessons = chapter.getLessons(); // Đã được fetch EAGERLY
                if (lessons != null) {
                    // Sắp xếp lesson theo orderNumber nếu cần
                    lessons.sort(Comparator.comparingInt(Lesson::getOrderNumber));

                    for (Lesson lesson : lessons) {
                        UserLesson progress = progressMap.get(lesson.getId());
                        lessonDTOs.add(new UserLessonLearningDTO(progress));
                    }
                }
                // Giả sử ChapterLearningDTO có constructor phù hợp
                chapterLearningDTOs.add(new ChapterLearningDTO(chapter, lessonDTOs));
            }
        }
        return chapterLearningDTOs;
    }
}
