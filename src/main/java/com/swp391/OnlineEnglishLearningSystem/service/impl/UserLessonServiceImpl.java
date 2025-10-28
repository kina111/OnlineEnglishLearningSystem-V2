package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserLessonLearningDTO;
import com.swp391.OnlineEnglishLearningSystem.repository.UserLessonRepository;
import com.swp391.OnlineEnglishLearningSystem.service.UserLessonService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserLessonServiceImpl implements UserLessonService {
    private final UserLessonRepository userLessonRepository;

    public UserLessonServiceImpl(UserLessonRepository userLessonRepository) {
        this.userLessonRepository = userLessonRepository;
    }

    @Override
    public void createFullUserLesson(Enrollment enrollment) {
        User currentUser = enrollment.getUser();
        Course currentCourse = enrollment.getCourse();
        List<Lesson> lessons = currentCourse.getChapters().stream().flatMap(c -> c.getLessons().stream()).toList();
        for (Lesson l : lessons) {
            UserLesson current = createNew(currentUser, enrollment, l);
        }
    }

    @Override
    public void updateIsCompleted(long userId, long lessonId) {
        UserLesson ul = this.userLessonRepository.findByUserIdAndLessonId(userId, lessonId).orElseThrow(() -> new IllegalArgumentException("UserLesson not found"));
        ul.setCompleted(true);
        this.userLessonRepository.save(ul);
    }

    public UserLesson createNew(User user, Enrollment enrollment, Lesson lesson){
        return this.userLessonRepository.save(new UserLesson(user, lesson, enrollment));
    }
}
