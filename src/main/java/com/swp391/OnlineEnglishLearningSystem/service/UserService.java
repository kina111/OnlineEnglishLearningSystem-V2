package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import jakarta.validation.Valid;

public interface UserService {
    void ensureEmailNotExists(String email);

    void save(User newUser);

    User buildNewUser(@Valid UserDTO userDTO);

    User findByEmailAndEnabledTrue(String email);

    void updatePassword(User user, String password);

    boolean isOldPasswordCorrect(User currentUser, String oldPassword);

    User getById(Long userId);
}
