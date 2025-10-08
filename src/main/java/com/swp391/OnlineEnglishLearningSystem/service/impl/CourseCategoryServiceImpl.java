package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.repository.CourseCategoryRepository;
import com.swp391.OnlineEnglishLearningSystem.service.CourseCategoryService;
import com.swp391.OnlineEnglishLearningSystem.service.specification.CourseCategorySpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    private final CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryServiceImpl(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Override
    public List<CourseCategory> findAll() {
        return this.courseCategoryRepository.findAll();
    }

    @Override
    public void ensureNotDuplicateName(String name) {
        List<CourseCategory> courseCategories = this.courseCategoryRepository.findAll();
        int cnt = 0;
        name = name.toLowerCase();
        for (CourseCategory courseCategory : courseCategories) {
            if (courseCategory.getName().toLowerCase().equals(name)) {
                cnt++;
                if (cnt > 1) throw new IllegalArgumentException("Course category with name " + name + " already exists");
            }
        }
    }

    @Override
    public void save(CourseCategory courseCategory) {
        this.courseCategoryRepository.save(courseCategory);
    }

    @Override
    public CourseCategory getById(Long id) {
        return this.courseCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course category not found"));
    }

    @Override
    public Page<CourseCategory> getCourseCategoriesWithSpecs(Pageable pageable, Boolean active, String search) {
        Specification<CourseCategory> spec = CourseCategorySpecs.searchByactive(active).and(CourseCategorySpecs.searchByName(search));
        return this.courseCategoryRepository.findAll(spec, pageable);
    }
}
