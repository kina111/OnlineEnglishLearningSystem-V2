package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Course;
import com.swp391.OnlineEnglishLearningSystem.model.Token;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.BlogDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.CourseFeedbackStats;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import com.swp391.OnlineEnglishLearningSystem.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final CourseService courseService;
    private final BlogService blogService;
    private final FeedbackService feedbackService;

    public AuthController(UserService userService, TokenService tokenService, EmailService emailService, CourseService courseService, BlogService blogService, FeedbackService feedbackService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.courseService = courseService;
        this.blogService = blogService;
        this.feedbackService = feedbackService;
    }

    // ---------------- HOME ----------------
    @GetMapping("/")
    public String home(Model model) {
        List<Course> featuredCourses = this.courseService.findFeaturedCourses(6);
        List<BlogDTO> latestBlogs = this.blogService.findLatestBlogs(4);

        Map<Long, CourseFeedbackStats> courseFeedbackStatsMap = new HashMap<>();
        for (Course course : featuredCourses) {
            Long courseId = course.getId();
            CourseFeedbackStats cfs = this.feedbackService.getFeedbackStats(courseId);
            courseFeedbackStatsMap.put(courseId, cfs);
        }

        model.addAttribute("courseFeedbackStatsMap", courseFeedbackStatsMap);
        model.addAttribute("latestBlogs", latestBlogs);
        model.addAttribute("featuredCourses", featuredCourses);
        return "home";
    }
    // ---------------- REGISTER ----------------

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.ensureEmailNotExists(userDTO.getEmail());
            User newUser = userService.buildNewUser(userDTO);
            Token newToken = tokenService.create(newUser);
            emailService.sendTokenEmail(newUser.getEmail(), newToken.getToken(), EmailService.EmailType.REGISTER);

            redirectAttributes.addFlashAttribute("message",
                    "Account created successfully. Please check your email for verification.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    // ---------------- EMAIL CONFIRMATION ----------------

    @GetMapping("/confirmToken")
    public String confirmToken(@RequestParam("token") String tokenValue,
                               RedirectAttributes redirectAttributes) {
        try {
            Token token = tokenService.checkValidToken(tokenValue);
            token.setConfirmed_at(LocalDateTime.now());
            tokenService.save(token);

            User user = token.getUser();
            user.setEnabled(true);
            userService.save(user);

            redirectAttributes.addFlashAttribute("message",
                    "Your account has been confirmed. You can now login!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/login";
    }

    // ---------------- LOGIN ----------------

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/login";
    }

    // ---------------- FORGOT PASSWORD ----------------
    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "auth/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPasswordForm(@RequestParam("email") String email,
                                            RedirectAttributes redirectAttributes) {
        try{
            User user = userService.findByEmailAndEnabledTrue(email).orElseThrow();
            Token token = tokenService.create(user);
            emailService.sendTokenEmail(user.getEmail(), token.getToken(), EmailService.EmailType.FORGOT_PASSWORD);
            redirectAttributes.addFlashAttribute("message",
                    "Please check your email for resetting your password!");

        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error",
                    "Email is not registered or not activated!");
        }
        return "redirect:/forgotPassword";
    }

// ---------------- RESET PASSWORD ----------------

    @GetMapping("/resetPassword")
    public String showResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "auth/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String processReset(@RequestParam("token") String tokenValue,
                               @RequestParam("password") String password,
                               @RequestParam("confirmedPassword") String confirmedPassword,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            if (!password.equals(confirmedPassword)) {
                throw new IllegalArgumentException("Passwords do not match!");
            }
            Token token = tokenService.checkValidToken(tokenValue);
            token.setConfirmed_at(LocalDateTime.now());
            tokenService.save(token);

            User user = token.getUser();
            userService.updatePassword(user, password);

            tokenService.delete(token);
            userService.save(user);

            redirectAttributes.addFlashAttribute("message",
                    "Password updated successfully. Please login with your new password.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", tokenValue);
            return "auth/resetPassword";
        }
    }

    //---------------------------- CHANGE PASSWORD -----------------------------
    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model) {
        return "auth/changePassword";
    }

    @PostMapping("/changePassword")
    public String processChangePasswordForm(@RequestParam("oldPassword") String oldPassword,
                                            @RequestParam("newPassword") String newPassword,
                                            @RequestParam("confirmedPassword") String confirmedPassword,
                                            RedirectAttributes redirectAttributes,
                                            Principal principal,
                                            Model model) {
        try {
            if (!newPassword.equals(confirmedPassword)) {
                throw new IllegalArgumentException("Passwords do not match!");
            }
            User currentUser = userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow();
            if (!userService.isOldPasswordCorrect(currentUser, oldPassword)){
                throw new IllegalArgumentException("Old password is incorrect!");
            };
            userService.updatePassword(currentUser, newPassword);

            redirectAttributes.addFlashAttribute("message",
                    "Password updated successfully. Please login with your new password.");
            return "redirect:/login";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/changePassword";
        }
    }
}
