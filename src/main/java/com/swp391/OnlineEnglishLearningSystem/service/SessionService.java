package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private UserService userService;

    public void storeUserInSession(HttpServletRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        HttpSession session = request.getSession();
        String email = authentication.getName();

        // Kiểm tra xem user đã có trong session chưa
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.getEmail().equals(email)) {
            // Load user từ database và lưu vào session
            User user = userService.findByEmailAndEnabledTrue(email);
            if (user != null) {
                session.setAttribute("currentUserId", user.getId());
                System.out.println("✅ User stored in session: " + user.getEmail() +
                        " (Auth type: " + getAuthenticationType(authentication) + ")");
            }
        }
    }

    private String getAuthenticationType(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return "FORM_LOGIN";
        } else if (authentication instanceof RememberMeAuthenticationToken) {
            return "REMEMBER_ME";
        } else {
            return authentication.getClass().getSimpleName();
        }
    }
}
