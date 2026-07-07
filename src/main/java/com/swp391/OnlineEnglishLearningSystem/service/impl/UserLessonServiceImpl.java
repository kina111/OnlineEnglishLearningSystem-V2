package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.repository.UserLessonRepository;
import com.swp391.OnlineEnglishLearningSystem.service.UserLessonService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLessonServiceImpl implements UserLessonService {
    private final UserLessonRepository userLessonRepository;

    @Override
    public void createFullUserLesson(Enrollment enrollment) {
        User currentUser = enrollment.getUser();
        Course currentCourse = enrollment.getCourse();
        List<Lesson> lessons = currentCourse.getChapters().stream().flatMap(c -> c.getLessons().stream()).toList();
        for (Lesson l : lessons) {
            createNew(currentUser, enrollment, l);
        }
    }

    @Override
    public void updateIsCompleted(long userId, long lessonId) {
        UserLesson ul = this.userLessonRepository.findByUserIdAndLessonId(userId, lessonId).orElseThrow(() -> new IllegalArgumentException("UserLesson not found"));
        ul.setCompleted(true);
        this.userLessonRepository.save(ul);
    }

    @Override
    public boolean existsByLessonIdAndUserId(long lessonId, long userId) {
        UserLesson ul = this.userLessonRepository.findByUserIdAndLessonId(userId, lessonId).orElse(null);
        return ul==null;
    }

    public UserLesson createNew(User user, Enrollment enrollment, Lesson lesson){
        return this.userLessonRepository.save(new UserLesson(user, lesson, enrollment));
    }
}
