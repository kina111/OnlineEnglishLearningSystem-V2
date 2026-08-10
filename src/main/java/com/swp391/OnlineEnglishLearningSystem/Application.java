package com.swp391.OnlineEnglishLearningSystem;

import com.swp391.OnlineEnglishLearningSystem.config.StorageProperties;
import com.swp391.OnlineEnglishLearningSystem.repository.RoleRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {
    
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Chỉ cập nhật password nếu user đã tồn tại
            // Việc insert user mẫu đầu tiên nên thực hiện qua data.sql hoặc Flyway seed migration
            userRepository.findByEmail("admin@example.com")
                    .ifPresent(u -> { u.setPassword(passwordEncoder.encode("123")); userRepository.save(u); });
            userRepository.findByEmail("expert@example.com")
                    .ifPresent(u -> { u.setPassword(passwordEncoder.encode("123")); userRepository.save(u); });
            userRepository.findByEmail("marketing@example.com")
                    .ifPresent(u -> { u.setPassword(passwordEncoder.encode("123")); userRepository.save(u); });
            userRepository.findByEmail("user@example.com")
                    .ifPresent(u -> { u.setPassword(passwordEncoder.encode("123")); userRepository.save(u); });
            System.out.println(">>> CommandLineRunner chạy xong.");
        };
    }
}
