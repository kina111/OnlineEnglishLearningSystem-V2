package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.CourseCategory;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseDTO;
import com.swp391.OnlineEnglishLearningSystem.service.CourseCategoryService;
import com.swp391.OnlineEnglishLearningSystem.service.CourseService;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseCategoryService courseCategoryService;
    private final UploadService uploadService;
    private final CourseService courseService;

    public CourseController(CourseCategoryService courseCategoryService, UploadService uploadService, CourseService courseService) {
        this.courseCategoryService = courseCategoryService;
        this.uploadService = uploadService;
        this.courseService = courseService;
    }

    // ===================== CREATE COURSE ========================
    @RequestMapping("/create")
    public String getCreateCoursePage (Model model) {
        List<CourseCategory> courseCategories = courseCategoryService.findAll();

        model.addAttribute("courseCategories", courseCategories);
        model.addAttribute("courseDTO", new CourseDTO());
        return "course/formToCreate";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            List<CourseCategory> courseCategories = courseCategoryService.findAll();
            model.addAttribute("courseCategories", courseCategories);
            return "course/formToCreate";
        }
        try{
            Course newCourse = courseService.buildNewCourse(courseDTO);
            if (courseDTO.getThumbnailFile() != null && !courseDTO.getThumbnailFile().isEmpty()) {
                String thumbnailFileName = uploadService.uploadImage(courseDTO.getThumbnailFile(), "courses/thumbnails");
                newCourse.setThumbnail(thumbnailFileName);
            }
            courseService.save(newCourse);

            model.addAttribute("course", newCourse);
            return "courses/update";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/courses/create";
        }
    }


}
