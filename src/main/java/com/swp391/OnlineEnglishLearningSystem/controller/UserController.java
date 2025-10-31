package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.model.dto.*;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final CourseService courseSerive;
    private final WishlistService wishlistService;
    private final EnrollmentService enrollmentService;
    private final WishlistService wishlistServiceImpl;
    private final UserLessonService userLessonService;
    private final LearningService learningService;
    private final LessonService lessonService;
    private final NoteService noteService;

    public UserController(UserService userService, UploadService uploadService, CourseService courseSerive, WishlistService wishlistService, EnrollmentService enrollmentService, WishlistService wishlistServiceImpl, UserLessonService userLessonService, LearningService learningService, LessonService lessonService, NoteService noteService) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.courseSerive = courseSerive;
        this.wishlistService = wishlistService;
        this.enrollmentService = enrollmentService;
        this.wishlistServiceImpl = wishlistServiceImpl;
        this.learningService = learningService;
        this.userLessonService = userLessonService;
        this.lessonService = lessonService;
        this.noteService = noteService;
    }

    //================================== Profile Management ================================//
    @GetMapping("/viewProfile")
    public String getUserPage(Model model,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        try{
            User currentUser = userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow();
            model.addAttribute("user", currentUser);
            return "user/userProfile";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/updateProfile")
    public String showFormUpdate(Principal principal,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow();
            model.addAttribute("updatedUser", user);
            return "user/updateProfile";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading profile: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @PostMapping("/updateProfile")
    public String handleUpdateProfile(@Valid @ModelAttribute("updatedUser") User updatedUser,
            BindingResult bindingResult,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "user/updateProfile";
        }
        try{
            User user = userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow();

            user.setFullName(updatedUser.getFullName());
            user.setDob(updatedUser.getDob());
            user.setGender(updatedUser.getGender());
            user.setMobile(updatedUser.getMobile());
            user.setAddress(updatedUser.getAddress());

            if (avatarFile != null && !avatarFile.isEmpty()){
                // Upload avatar mới
                String avatarFileName = uploadService.uploadImage(avatarFile, "avatars");
                user.setAvatar(avatarFileName);
            }

            userService.save(user);
            redirectAttributes.addFlashAttribute("message", "Update profile successfully");
            return "redirect:/viewProfile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Update failed: " + e.getMessage());
            return "user/updateProfile";
        }
    }

    @PostMapping("/courses/{courseId}/changeWishlist")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> changeWishlistStatus(@PathVariable("courseId") Long courseId,
                                                            @RequestParam("add-to-wishlist") boolean addToWishlist,
                                                            HttpSession session){
        try{
            Long userId = (Long) session.getAttribute("currentUserId");
            User currentUser = this.userService.getUserById(userId);
            Course currentCourse = this.courseSerive.findById(courseId);
            if (!addToWishlist){
                Optional<Wishlist> wishlist = this.wishlistService.findByUserIdAndCourseId(userId, courseId);
                wishlist.ifPresent(value -> this.wishlistService.delete(value.getId()));
            }else{
                Wishlist newWishlist = this.wishlistService.createNew(userId, courseId);
            }
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Update to wishlist successfully!", null, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST,
                    "Update to wishlist failed!", null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{userId}/myCourses")
    public String getMyCourses(@PathVariable("userId") Long userId,
                               Model model){
        try{
            User user = this.userService.getUserById(userId);
            List<Enrollment> enrollments = this.enrollmentService.findByUserId(userId);
            List<Wishlist> wishlists = this.wishlistService.findByUserId(userId);

            model.addAttribute("enrollments", enrollments);
            model.addAttribute("wishlists", wishlists);
            model.addAttribute("user", user);
        }catch (Exception e){

        }
        return "user/myCourses";
    }

    // ====================================== GET LEARNING VIEW ===========================================
    //lấy thanh bar bên phải trên trang học bài
    @GetMapping("/users/{userId}/enrollments/{enrollmentId}/navbar")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<ChapterLearningDTO>>> getLearningView(
            @PathVariable("enrollmentId") long enrollmentId,
            @PathVariable("userId") long userId
            /* Bỏ HttpSession nếu không dùng trực tiếp ở đây nữa */
    ) {
        try {
            List<ChapterLearningDTO> chapterLearningDTOS = learningService.prepareLearningViewData(userId, enrollmentId);

            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Get learning view successfully!", chapterLearningDTOS, null), HttpStatus.OK);

        } catch (IllegalArgumentException e) { // Bắt lỗi cụ thể
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND,
                    e.getMessage(), null, null), HttpStatus.NOT_FOUND);
        } catch (Exception e) { // Bắt lỗi chung
            // Nên log lỗi chi tiết ở đây: log.error("...", e);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, // Dùng 500
                    "Get learning view failed!", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //lấy thông tin cho lesson hiện tại trên trang học bài
    @GetMapping("/users/{userId}/learning/lessons/{lessonId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<LessonLearningDTO>> getLessonLearningView(@PathVariable("lessonId") long lessonId){
        try{
            Lesson lesson = this.lessonService.findById(lessonId);
            LessonLearningDTO current = new LessonLearningDTO(lesson);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Get lesson learning view successfully!", current, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Get lesson learning view failed!", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //lấy thông tin cho header trên trang học bài
    @GetMapping("/api/enrollments/{enrollmentId}/progress")
    @ResponseBody
    public ResponseEntity<ApiResponse<EnrollmentLearningDTO>> getHeaderEnrollmentLearningView(@PathVariable("enrollmentId") long enrollmentId){
        try{
            EnrollmentLearningDTO current = this.enrollmentService.createEnrollmentDTO(enrollmentId);

            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Get header enrollment learning view successfully!", current, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Get header enrollment learning view failed!", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // api cập nhật isCompleted cho UserLesson
    @PutMapping("/api/lessons/{lessonId}/complete")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> updateIsCompleted(@PathVariable("lessonId") long lessonId,
                                                               HttpSession session){
        try{
            Long userId = (Long) session.getAttribute("currentUserId");
            this.userLessonService.updateIsCompleted(userId, lessonId);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Update isCompleted successfully!", null, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Update isCompleted failed!", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/enrollments/{enrollmentId}")
    public String getLearningView(@PathVariable("enrollmentId") long enrollmentId,
                                  HttpSession session, Model model){
        try{
            Long userId = (Long) session.getAttribute("currentUserId");
            //thanh bên phải
            List<ChapterLearningDTO> chapterLearningDTOS = learningService.prepareLearningViewData(userId, enrollmentId);
            //header
            EnrollmentLearningDTO enrollmentLearningDTO = this.enrollmentService.createEnrollmentDTO(enrollmentId);

            model.addAttribute("chapterLearningDTOS", chapterLearningDTOS);
            model.addAttribute("enrollmentLearningDTO", enrollmentLearningDTO);
            return "user/learningView";
        }catch (Exception e){
            return "redirect:/login";
        }
    }

    //nhận note và lưu
    @PostMapping("/api/lessons/{lessonId}/notes")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> takeNotes(@Valid @RequestBody NoteRequest noteRequest,
                                                       @PathVariable("lessonId") long lessonId,
                                                       HttpSession session){
        try{
            Long userId = (Long) session.getAttribute("currentUserId");
            Note newNote = this.noteService.createNew(userId, lessonId, noteRequest);

            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Take notes successfully!", null, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Take notes failed!", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //lấy notes theo tiêu chí
    @GetMapping("/api/enrollments/{enrollmentId}/chapters/{chapterId}/notes")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<NoteDTO>>> getNotes(@PathVariable("enrollmentId") long enrollmentId,
                                                               @PathVariable("chapterId") long chapterId,
                                                               @RequestParam(value = "isNewest") Boolean isNewest){
        try{
            Sort sort = isNewest
                    ? Sort.by(Sort.Direction.DESC, "createdAt")
                    : Sort.by(Sort.Direction.ASC, "createdAt");
            List<NoteDTO> noteDTOs = this.noteService.createDtosByChapterAndEnrollmentId(chapterId, enrollmentId, sort);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Take notes successfully!", noteDTOs, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Take notes failed!", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //xóa note
    @DeleteMapping("/api/notes/{noteId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> deleteNote(@PathVariable("noteId") long noteId,
                                                        HttpSession session){
        try{
            Long userId = (Long) session.getAttribute("currentUserId");
            if (userId == null){
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.UNAUTHORIZED,
                        "Unauthorized!", null, null), HttpStatus.UNAUTHORIZED);
            }
            this.noteService.deleteById(noteId);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Delete note successfully!", null, null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Delete note failed!", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/notes/{noteId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<String>> updateNote(@PathVariable("noteId") long noteId,
                                                        @RequestBody String content,
                                                        HttpSession session){
        try{
            Long userId = (Long) session.getAttribute("currentUserId");
            if (userId == null){
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.UNAUTHORIZED,
                        "Unauthorized!", null, null), HttpStatus.UNAUTHORIZED);
            }
            Note note = this.noteService.updateById(noteId, content);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK,
                    "Update note successfully", note.getContent(), null), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Update note failed!", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
                                                        }
}
