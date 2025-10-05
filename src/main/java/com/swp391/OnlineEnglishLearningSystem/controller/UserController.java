package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserProfileDTO;
import com.swp391.OnlineEnglishLearningSystem.service.FileStorageService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import com.swp391.OnlineEnglishLearningSystem.service.impl.FileStorageServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final FileStorageService storageService;

    public UserController(UserService userService,
                          FileStorageServiceImpl storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    //================================== Profile Management ================================//
    @GetMapping("/viewProfile")
    public String getUserPage(Principal principal,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try{
            User user = userService.findByEmailAndEnabledTrue(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            model.addAttribute("user", user);
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
      try{
          User user = userService.findByEmailAndEnabledTrue(principal.getName())
                  .orElseThrow(() -> new IllegalStateException("User not found"));
          model.addAttribute("currentUser", user);
          model.addAttribute("updatedUserDTO", new UserProfileDTO());
          return "user/updateProfile";
      }catch (Exception e){
          redirectAttributes.addFlashAttribute("error", e.getMessage());
          return "redirect:/login";
      }
    }

    @PostMapping("/updateProfile")
    public String handleUpdateProfile(
            @ModelAttribute("updatedUserDTO") UserProfileDTO updatedUserDTO,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        // lưu file
        try{
            if (!avatarFile.isEmpty()) {
                // Lưu file vào thư mục avatars/
                String fileName = storageService.saveToFolder(avatarFile, "avatars/");
                updatedUserDTO.setAvatar(fileName); // gán tên file vào DTO
            }

            userService.updateUser(principal.getName(), updatedUserDTO);
            return "redirect:/viewProfile";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/updateProfile";
        }
    }
}
