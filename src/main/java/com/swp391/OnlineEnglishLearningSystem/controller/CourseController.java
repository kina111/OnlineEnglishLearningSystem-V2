package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.ApiResponse;
import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.model.Enrollment;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseFeedbackStats;
import com.swp391.OnlineEnglishLearningSystem.model.dto.FeedbackDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UpdateCourseDTO;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import com.swp391.OnlineEnglishLearningSystem.service.impl.EnrollmentServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseCategoryService courseCategoryService;
    private final CourseService courseService;
    private final UserService userService;
    private final EnrollmentServiceImpl enrollmentService;
    private final WishlistService wishlistService;
    private final FeedbackService feedbackService;

    public CourseController(CourseCategoryService courseCategoryService, CourseService courseService, UserService userService, EnrollmentServiceImpl enrollmentService, WishlistService wishlistService, FeedbackService feedbackService) {
        this.courseCategoryService = courseCategoryService;
        this.courseService = courseService;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
        this.wishlistService = wishlistService;
        this.feedbackService = feedbackService;
    }

    // ===================== GET COURSES =========================
    @GetMapping("/learner")
    public String getCoursesLearnerPage(@RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) String keyword,
                                        @PageableDefault(page = 0, size = 6, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                        HttpSession session, Model model){
        Page<Course> coursePage = this.courseService.findCoursesByAuthorAndFilters(null, Course.CourseStatus.PUBLISHED, categoryId, keyword, pageable);
        List<Course> featuredCourses = this.courseService.findFeaturedCourses(3);

        model.addAttribute("coursePage", coursePage);
        model.addAttribute("allCategories", this.courseCategoryService.findAll());
        model.addAttribute("featuredCourses", featuredCourses);
        model.addAttribute("currentCategoryId", categoryId); // For highlighting active category
        model.addAttribute("currentKeyword", keyword); // For search box value

        return "user/viewCourseList";
    }

    //show course details for learner
    @GetMapping("/{courseId}/learner")
    public String getCourseDetailsForLearner(@PathVariable("courseId") Long courseId,
                                             HttpSession session,
                                             Model model){
        try{
            Course course = this.courseService.findById(courseId);
            Long userId = (Long) session.getAttribute("currentUserId");
            Enrollment e = this.enrollmentService.findByUserIdAndCourseId(userId, courseId);
            boolean inWishlist = this.wishlistService.findByUserIdAndCourseId(userId, courseId).isPresent();
            Page<FeedbackDTO> initialFeedbacks = this.feedbackService.getApprovedFeedbacks(courseId, PageRequest.of(0, 5, Sort.by("rating").descending()));
            CourseFeedbackStats courseFeedbackStats = this.feedbackService.getFeedbackStats(courseId);

            if (e != null) model.addAttribute("currentEnrollmentId", e.getId());
            model.addAttribute("courseFeedbackStats", courseFeedbackStats);
            model.addAttribute("initialFeedbacks", initialFeedbacks);
            model.addAttribute("inWishlist", inWishlist);
            model.addAttribute("isEnrolled", e != null);
            model.addAttribute("course", course);
            return "user/viewCourseDetails";
        }catch (Exception e){
            return "home";
        }
    }

    @GetMapping("/admin")
    public String getCoursesAdminPage(@RequestParam(required = false) Long expertId,
                                      @RequestParam(required = false) Course.CourseStatus status,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) String keyword,
                                      // tự động lấy tham số page, size, sort từ URL
                                      @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                      Model model){
        Page<Course> coursePage = this.courseService.findCoursesByAuthorAndFilters(expertId, status, categoryId, keyword, pageable);
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("currentExpertId", expertId);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("allStatuses", Course.CourseStatus.values());
        model.addAttribute("allCategories", this.courseCategoryService.findAll());
        model.addAttribute("allExperts", this.userService.getUsersByRoleName("ROLE_EXPERT"));
        return "admin/course/courseDashboard";
    }

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

            return "redirect:/courses/" + newCourse.getId();
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

    @PostMapping("/{courseId}")
    public String handleAdminRespond(@PathVariable("courseId") Long courseId,
                                     @RequestParam("admin-respond") String respondToPublish,
                                     RedirectAttributes redirectAttributes){
        try{
            Course courseToHandle = this.courseService.handleChangingCourseStatus(courseId, respondToPublish);
            redirectAttributes.addFlashAttribute("message", "Handle course status success!");
            return "redirect:/courses/admin";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/courses/admin";
        }
    }

    @PatchMapping("/api/{courseId}/toggle-featured")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> toggleFeatured(@PathVariable("courseId") Long courseId,
                                                            @RequestBody Map<String, Boolean> featuredStatus){
        Boolean featured = featuredStatus.get("featured");
        if (featured == null) {
            // Trả về lỗi nếu không có trạng thái 'featured' trong request body
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing 'featured' status in request body", null, "BAD_REQUEST");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            this.courseService.updateFeaturedStatus(courseId, featured); // Gọi service để cập nhật
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK, "Cập nhật trạng thái featured thành công", null, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Cập nhật trạng thái featured không thành công!", null, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/{courseId}/feedback-fragment")
    public String getCourseFeedbackFragment(@PathVariable("courseId") Long courseId,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "ratingFilter", required = false) Integer ratingFilter,
                                            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                                            Model model){

        try{
            Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
            Page<FeedbackDTO> feedbacks = this.feedbackService.getApprovedFeedbacksWithSpecs(courseId, pageable, ratingFilter, searchKeyword);

            model.addAttribute("feedbacks", feedbacks);
            model.addAttribute("newTotalPages", feedbacks.getTotalPages());
            return "components/_feedback_cards :: feedbackCardList";
        }catch (Exception e){
            return ""; //tạm thời
        }
    }
}
