package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.*;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

    public UserController(UserService userService, UploadService uploadService, CourseService courseSerive, WishlistService wishlistService, EnrollmentService enrollmentService, WishlistService wishlistServiceImpl) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.courseSerive = courseSerive;
        this.wishlistService = wishlistService;
        this.enrollmentService = enrollmentService;
        this.wishlistServiceImpl = wishlistServiceImpl;
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
                Optional<Wishlist> wishlist = this.wishlistServiceImpl.findByUserAndCourse(currentUser, currentCourse);
                wishlist.ifPresent(this.wishlistService::delete);
            }else{
                Wishlist newWishlist = this.wishlistService.createNew(currentUser, currentCourse);
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
            List<Enrollment> enrollments = this.enrollmentService.findByUser(user);
            List<Wishlist> wishlists = this.wishlistService.findByUser(user);

            model.addAttribute("enrollments", enrollments);
            model.addAttribute("wishlists", wishlists);
            model.addAttribute("user", user);
        }catch (Exception e){

        }
        return "user/myCourses";
    }
}
