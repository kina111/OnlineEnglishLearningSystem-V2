package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.UserRole;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import com.swp391.OnlineEnglishLearningSystem.repository.RoleRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserRepository;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void ensureEmailNotExists(String email) {
        userRepository.findByEmail(email)
                .ifPresent(u -> { throw new IllegalArgumentException("Email already exists"); });
    }

    @Override
    public void save(User newUser) {
        userRepository.save(newUser);
    }

    @Override
    public User buildNewUser(UserDTO userDTO) {
        UserRole roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Default role not found"));

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFullName(userDTO.getFullName());
        user.setRole(roleUser);
        user.setEnabled(false);
        return user;
    }
}
