package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.UserRole;
import com.swp391.OnlineEnglishLearningSystem.model.dto.UserDTO;
import com.swp391.OnlineEnglishLearningSystem.repository.RoleRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserRepository;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import com.swp391.OnlineEnglishLearningSystem.service.specification.UserSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Override
    public User findByEmailAndEnabledTrue(String email) {
        Optional<User> user = userRepository.findByEmailAndEnabledTrue(email);
        return user.orElse(null);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }

    @Override
    public boolean isOldPasswordCorrect(User currentUser, String oldPassword) {
        return passwordEncoder.matches(oldPassword, currentUser.getPassword());
    }

    @Override
    public User getById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> findPaginated(Pageable pageable) {
        List<User> users = userRepository.findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;
        if (users.size() < startItem) {
            list = Collections.emptyList();
        }else{
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, pageable, users.size());
    }

    public Page<User> getUsersWithSpecs(Pageable pageable, String gender, String role, Boolean enabled, String search) {
        Specification<User> spec = UserSpecs.withFilters(search, gender, role, enabled);
        return userRepository.findAll(spec, pageable);
    }


}
