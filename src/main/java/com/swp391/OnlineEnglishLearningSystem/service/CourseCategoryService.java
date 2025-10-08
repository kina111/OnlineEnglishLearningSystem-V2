package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseCategoryService {
    List<CourseCategory> findAll();

    void ensureNotDuplicateName(String name);

    void save(@Valid CourseCategory courseCategory);

    CourseCategory getById(Long id);

    Page<CourseCategory> getCourseCategoriesWithSpecs(Pageable pageable, Boolean active, String search);
}
