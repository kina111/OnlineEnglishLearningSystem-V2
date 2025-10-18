package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import com.swp391.OnlineEnglishLearningSystem.service.CourseCategoryService;
import com.swp391.OnlineEnglishLearningSystem.service.CourseService;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseCategoryService courseCategoryService;
    private final UploadService uploadService;
    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseCategoryService courseCategoryService, UploadService uploadService, CourseService courseService, UserService userService) {
        this.courseCategoryService = courseCategoryService;
        this.uploadService = uploadService;
        this.courseService = courseService;
        this.userService = userService;
    }

    // ===================== GET COURSES =========================
    @GetMapping("/users/{expertId}")
    public String getCoursesByUserPage(
            @PathVariable("expertId") Long userId,
            @RequestParam(required = false) Course.CourseStatus status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            // tự động lấy tham số page, size, sort từ URL
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) { // Inject Model to pass data to the view

        // 1. Call the service method to get the paginated data
        Page<Course> coursePage = courseService.findCoursesByAuthorAndFilters(userId, status, categoryId, keyword, pageable);

        // 2. Add data to the Model for Thymeleaf
        model.addAttribute("coursePage", coursePage); // The Page object containing courses and pagination info
        model.addAttribute("expertId", userId); // Pass the expertId back for links
        model.addAttribute("currentStatus", status); // Pass current filters back for display/forms
        model.addAttribute("currentCategoryId", categoryId); // Pass current filters back for display/forms
        model.addAttribute("currentKeyword", keyword);

        //info for dropdown list
        model.addAttribute("allStatuses", Course.CourseStatus.values());
        model.addAttribute("allCategories", courseCategoryService.findAll());

        // 3. Return the name of the Thymeleaf template
        return "course/expertDashboard"; // Name of your HTML file (e.g., user-course-list.html)
    }
    // ===================== CREATE COURSE ========================
    @RequestMapping("/create")
    public String getCreateCoursePage (Model model) {
        List<CourseCategory> courseCategories = courseCategoryService.findAll();

        model.addAttribute("courseCategories", courseCategories);
        model.addAttribute("courseDTO", new CourseDTO());
        return "course/createNewCourseForm";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            List<CourseCategory> courseCategories = courseCategoryService.findAll();
            model.addAttribute("courseCategories", courseCategories);
            return "course/createNewCourseForm";
        }
        try{
            Course newCourse = courseService.buildNewCourse(courseDTO);
            if (courseDTO.getThumbnailFile() != null && !courseDTO.getThumbnailFile().isEmpty()) {
                String thumbnailFileName = uploadService.uploadImage(courseDTO.getThumbnailFile(), "courses/thumbnails");
                newCourse.setThumbnail(thumbnailFileName);
            }
            newCourse.setAuthor(userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow());
            courseService.save(newCourse);

            model.addAttribute("course", newCourse);
            return "course/viewAndUpdateChapter";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/courses/create";
        }
    }

    @GetMapping("/{id}")
    public String getCoursePage(Model model,
                                @PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes,
                                Principal principal) {
        try{
            Course course = courseService.findById(id);
            model.addAttribute("course", course);
            return "course/viewAndUpdateChapter";
        }catch (Exception e) {
            return "home";
        }
    }
}
