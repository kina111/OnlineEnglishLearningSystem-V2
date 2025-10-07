package com.swp391.OnlineEnglishLearningSystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    @Async
    public void sendTokenEmail(String to, String token, EmailType type) {
        String subject = type.getSubject();
        String text = buildEmailContent(token, type);
        sendEmail(to, subject, text);
    }

    public String buildEmailContent(String password) {
        String url = "http://localhost:8080/login";
        return String.format("""
                Hello!

                The admin has created a new account for you. Please login with the following password:
                %s
                Please log in via the link below:
                %s

                This link will expire in 24 hours.

                If you did not request this, please ignore this email.
                """, password, url);
    }
    private String buildEmailContent(String token, EmailType type) {
        String url = switch (type) {
            case REGISTER -> "http://localhost:8080/confirmToken?token=" + token;
            case FORGOT_PASSWORD -> "http://localhost:8080/resetPassword?token=" + token;
        };

        return String.format("""
                Hello!

                Please confirm your action by clicking the link below:
                %s

                This link will expire in 24 hours.

                If you did not request this, please ignore this email.
                """, url);
    }

    // Enum để tránh hard-code string
    public enum EmailType {
        REGISTER("Welcome to Online English Learning System!"),
        FORGOT_PASSWORD("Reset Password");

        private final String subject;

        EmailType(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }
    }
}
