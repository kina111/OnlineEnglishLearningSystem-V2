package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UpdateCourseDTO;
import com.swp391.OnlineEnglishLearningSystem.repository.CourseCategoryRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.CourseRepository;
import com.swp391.OnlineEnglishLearningSystem.service.CourseService;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import com.swp391.OnlineEnglishLearningSystem.service.specification.CourseSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseCategoryRepository courseCategoryRepository;
    private final CourseRepository courseRepository;
    private final UploadService uploadService;
    private final UserService userService;

    public CourseServiceImpl(CourseCategoryRepository courseCategoryRepository, CourseRepository courseRepository, UploadService uploadService, UserService userService) {
        this.courseCategoryRepository = courseCategoryRepository;
        this.courseRepository = courseRepository;
        this.uploadService = uploadService;
        this.userService = userService;
    }

    @Override
    public Course buildNewCourse(CourseDTO courseDTO, Long authorId) {
        User author = this.userService.getUserById(authorId);
        if (this.courseRepository.existsByName(courseDTO.getName())) throw new IllegalArgumentException("Tên khóa học " + courseDTO.getName() + " đã tồn tại.");
        if (courseDTO.getThumbnailFile() == null && courseDTO.getThumbnailFile().isEmpty()) throw new IllegalArgumentException("Course thumbnail is required");

        Course course = new Course();
        String thumbnailUrl = this.uploadService.uploadImage(courseDTO.getThumbnailFile(), "courses/thumbnails");
        course.setThumbnail(thumbnailUrl);
        course.setName(courseDTO.getName());
        course.setCategory(courseCategoryRepository.getReferenceById(courseDTO.getCategoryId()));
        course.setShortDescription(courseDTO.getShortDescription());
        course.setDescription(courseDTO.getDescription());
        course.setPrerequisite(courseDTO.getPrerequisite());
        course.setPrice(courseDTO.getPrice());
        course.setDiscount(courseDTO.getDiscount());
        course.setAuthor(author);

        course.setFeatured(false);
        course.setStatus(Course.CourseStatus.DRAFT);
        return this.courseRepository.save(course);
    }

    @Override
    public void save(Course newCourse) {
        this.courseRepository.save(newCourse);
    }

    @Override
    public Course findById(Long id) {
        return this.courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    @Override
    public Page<Course> findCoursesByAuthorAndFilters(Long userId, Course.CourseStatus status, Long categoryId, String keyword, Pageable pageable) {
        Specification<Course> spec = CourseSpecs.hasAuthorId(userId).and(CourseSpecs.hasStatus(status)).and(CourseSpecs.hasCategoryId(categoryId)).and(CourseSpecs.hasNameContaining(keyword));
        return this.courseRepository.findAll(spec, pageable);
    }

    @Override
    public Course updateCourse(Long id, UpdateCourseDTO updateDto) {
        Course course = this.courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        course.setName(updateDto.getName());
        course.setShortDescription(updateDto.getShortDescription());
        course.setDescription(updateDto.getDescription());
        course.setPrerequisite(updateDto.getPrerequisite());
        course.setPrice(updateDto.getPrice());
        course.setDiscount(updateDto.getDiscount());
        course.setCategory(courseCategoryRepository.getReferenceById(updateDto.getCategoryId()));
        if (updateDto.getThumbnailFile() != null && !updateDto.getThumbnailFile().isEmpty()) {
            String newThumbnailUrl = this.uploadService.uploadImage(updateDto.getThumbnailFile(), "courses/thumbnails");
            course.setThumbnail(newThumbnailUrl);
            updateDto.setCurrentThumbnailUrl(newThumbnailUrl);
        }
        return this.courseRepository.save(course);
    }

    @Override
    public void sendSubmitReview(Long courseId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        switch (course.getStatus()) {
            case DRAFT:
                course.setStatus(Course.CourseStatus.PENDING);
                break;
            case PENDING:
                throw new IllegalArgumentException("Course is already pending");
            case PUBLISHED:
                throw new IllegalArgumentException("Course is already published");
            default:
        }
        this.courseRepository.save(course);
    }

    @Override
    public void cancelReview(Long courseId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        switch (course.getStatus()) {
            case DRAFT:
                throw new IllegalArgumentException("Course is not in draft status");
            case PENDING:
                course.setStatus(Course.CourseStatus.DRAFT);
                break;
            case PUBLISHED:
                throw new IllegalArgumentException("Course is already published");
            default:
        }
        this.courseRepository.save(course);
    }

    @Override
    public Course deleteById(Long courseId) {
        Course c = this.courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        if (c.getStatus() != Course.CourseStatus.DRAFT)  throw new IllegalArgumentException("Course is not in draft status");
        this.courseRepository.delete(c);
        return c;
    }

    @Override
    public Course handleChangingCourseStatus(Long courseId, String respondToPublish) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        switch (respondToPublish) {
            case "true":
                if (course.getStatus() == Course.CourseStatus.PUBLISHED) throw new IllegalArgumentException("Course is already published");
                course.setStatus(Course.CourseStatus.PUBLISHED);
                return this.courseRepository.save(course);
            case "false":
                if (course.getStatus() == Course.CourseStatus.DRAFT) throw new IllegalArgumentException("Course is already draft");
                else if (course.getStatus() == Course.CourseStatus.PUBLISHED) throw new IllegalArgumentException("Course was published, cannot be drafted");
                course.setStatus(Course.CourseStatus.DRAFT);
                return this.courseRepository.save(course);
        }
        return null;
    }

    @Override
    public void updateFeaturedStatus(Long courseId, Boolean featured) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        course.setFeatured(featured);
        this.courseRepository.save(course);
    }

    @Override
    public List<Course> findFeaturedCourses(int quantity) {
        List<Course> courses = this.courseRepository.findAllByFeaturedTrue();
        if (courses.size() < quantity) return courses;
        else return courses.subList(0, quantity);
    }
}
