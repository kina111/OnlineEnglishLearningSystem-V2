package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import com.swp391.OnlineEnglishLearningSystem.repository.CourseCategoryRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.CourseRepository;
import com.swp391.OnlineEnglishLearningSystem.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseCategoryRepository courseCategoryRepository;
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseCategoryRepository courseCategoryRepository, CourseRepository courseRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Course buildNewCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setCategory(courseCategoryRepository.getReferenceById(courseDTO.getCategoryId()));
        course.setShortDescription(courseDTO.getShortDescription());
        course.setDescription(courseDTO.getDescription());
        course.setPrerequisite(courseDTO.getPrerequisite());
        course.setPrice(courseDTO.getPrice());
        course.setDiscount(courseDTO.getDiscount());

        course.setFeatured(false);
        course.setStatus(Course.CourseStatus.DRAFT);
        return course;
    }

    @Override
    public void save(Course newCourse) {
        this.courseRepository.save(newCourse);
    }

    @Override
    public Course findById(Long id) {
        return this.courseRepository.findById(id).orElseThrow();
    }
}
