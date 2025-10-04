package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
