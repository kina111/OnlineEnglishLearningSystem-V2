package com.swp391.OnlineEnglishLearningSystem.model.dto;

import com.swp391.OnlineEnglishLearningSystem.util.PasswordMatches;
import com.swp391.OnlineEnglishLearningSystem.util.ValidEmail;
import jakarta.validation.constraints.NotBlank;

@PasswordMatches
public class UserDTO {
    @NotBlank
    private String fullName;

    @ValidEmail
    @NotBlank
    private String email;

    @NotBlank
    private String password;
    private String confirmedPassword;


    public UserDTO() {
    }

    public UserDTO(String fullName, String email, String password, String confirmedPassword) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }
}
