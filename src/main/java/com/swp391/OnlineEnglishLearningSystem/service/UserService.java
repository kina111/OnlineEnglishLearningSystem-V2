package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import jakarta.validation.Valid;

import java.util.Optional;

public interface UserService {
    void ensureEmailNotExists(String email);

    void save(User newUser);

    User buildNewUser(@Valid UserDTO userDTO);

    Optional<User> findByEmailAndEnabledTrue(String email);

    void updatePassword(User user, String password);
}
