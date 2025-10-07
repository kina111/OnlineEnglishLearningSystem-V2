package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
//
public interface UserService {
    void ensureEmailNotExists(String email);

    void save(User newUser);

    User buildNewUser(@Valid UserDTO userDTO);

    User findByEmailAndEnabledTrue(String email);

    void updatePassword(User user, String password);

    boolean isOldPasswordCorrect(User currentUser, String oldPassword);

    User getById(Long userId);

    List<User> getAllUsers();

    User getUserById(Long id);

    void deleteById(Long id);

    Page<User> findPaginated(Pageable pageable);

    Page<User> getUsersWithSpecs(Pageable pageale, String gender, String role, Boolean enabled, String search);
}
