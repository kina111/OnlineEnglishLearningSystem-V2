package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;

    public UserController(UserService userService, UploadService uploadService) {
        this.userService = userService;
        this.uploadService = uploadService;
    }

    //================================== Profile Management ================================//
    @GetMapping("/viewProfile")
    public String getUserPage(Model model,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        try{
            User currentUser = userService.findByEmailAndEnabledTrue(principal.getName());
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
            User user = userService.findByEmailAndEnabledTrue(principal.getName());
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Please login first");
                return "redirect:/login";
            }

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
            User user = userService.findByEmailAndEnabledTrue(principal.getName());

            user.setFullName(updatedUser.getFullName());
            user.setDob(updatedUser.getDob());
            user.setGender(updatedUser.getGender());
            user.setMobile(updatedUser.getMobile());
            user.setAddress(updatedUser.getAddress());

            if (avatarFile != null && !avatarFile.isEmpty()){
                // Upload avatar mới
                String avatarFileName = uploadService.uploadImage(avatarFile);
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
}
