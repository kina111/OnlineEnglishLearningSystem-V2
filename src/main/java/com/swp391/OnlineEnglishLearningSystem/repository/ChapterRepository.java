package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByCourseIdOrderByOrderNumberAsc(Long courseId);
}
