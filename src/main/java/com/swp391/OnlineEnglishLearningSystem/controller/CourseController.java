package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UpdateCourseDTO;
import com.swp391.OnlineEnglishLearningSystem.service.CourseCategoryService;
import com.swp391.OnlineEnglishLearningSystem.service.CourseService;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import jakarta.servlet.http.HttpSession;
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

    // ===================== CREATE COURSE ========================
    @RequestMapping("/create")
    public String getCreateCoursePage (Model model) {
        List<CourseCategory> courseCategories = courseCategoryService.findAll();
        model.addAttribute("isUpdate", false);
        model.addAttribute("isReadOnly", false);
        model.addAttribute("courseCategories", courseCategories);
        model.addAttribute("courseDTO", new CourseDTO());
        return "course/createNewCourseForm";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            List<CourseCategory> courseCategories = courseCategoryService.findAll();
            model.addAttribute("courseCategories", courseCategories);
            model.addAttribute("isUpdate", false);
            model.addAttribute("isReadOnly", false);
            return "course/createNewCourseForm";
        }
        try{
            Long authorId = (Long) session.getAttribute("currentUserId");
            Course newCourse = courseService.buildNewCourse(courseDTO, authorId);

            model.addAttribute("course", newCourse);
            return "course/viewAndUpdateChapter";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/courses/create";
        }
    }

    //====================== UPDATE COURSE =========================
    @GetMapping("/{id}/update")
    public String showUpdateCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);

        // Tạo UpdateDTO từ Course entity
        UpdateCourseDTO updateDto = new UpdateCourseDTO(course);

        model.addAttribute("courseDTO", updateDto); // Dùng tên chung để form tái sử dụng
        model.addAttribute("isUpdate", true);
        model.addAttribute("isReadOnly", updateDto.getStatus() != Course.CourseStatus.DRAFT);
        model.addAttribute("courseId", id);
        model.addAttribute("courseCategories", courseCategoryService.findAll());

        return "course/createNewCourseForm";
    }

    @PostMapping("/{id}/update")
    public String updateCourse(
            @PathVariable Long id,
            @Valid @ModelAttribute("courseDTO") UpdateCourseDTO updateDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("courseDTO", updateDto);
            model.addAttribute("courseId", id);
            model.addAttribute("isUpdate", true);
            model.addAttribute("courseCategories", this.courseCategoryService.findAll());
            return "course/createNewCourseForm";
        }

        Course coursetoUpdate = this.courseService.updateCourse(id, updateDto);
        return "redirect:/courses/users/" + coursetoUpdate.getAuthor().getId();
    }

    //============= SUBMIT-REVIEW =================
    @PostMapping("/{courseId}/submit-review")
    public String sendSubmitReview(@PathVariable("courseId") Long courseId,
                                   Model model,
                                   HttpSession session, RedirectAttributes redirectAttributes){
        try{
            this.courseService.sendSubmitReview(courseId);
            redirectAttributes.addFlashAttribute("message", "Send submit review success!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/courses/users/" + session.getAttribute("currentUserId");
    }

    @PostMapping("/{courseId}/delete")
    public String deleteCourse(@PathVariable("courseId") Long courseId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes){
        try{
            Course courseToDelete = this.courseService.deleteById(courseId);
            redirectAttributes.addFlashAttribute("message", "Delete course success!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/courses/users/" + session.getAttribute("currentUserId");
    }
}
