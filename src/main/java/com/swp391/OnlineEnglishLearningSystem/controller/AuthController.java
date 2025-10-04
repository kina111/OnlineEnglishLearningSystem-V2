package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Token;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import com.swp391.OnlineEnglishLearningSystem.service.EmailService;
import com.swp391.OnlineEnglishLearningSystem.service.TokenService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;

    public AuthController(UserService userService, TokenService tokenService, EmailService emailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
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
            userService.save(newUser);
            Token newToken = tokenService.create(newUser);
            tokenService.save(newToken);
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

            redirectAttributes.addFlashAttribute("message",
                    "Your account has been confirmed. You can now login!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/login";
    }
}
