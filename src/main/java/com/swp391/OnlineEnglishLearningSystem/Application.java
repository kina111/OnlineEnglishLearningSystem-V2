package com.swp391.OnlineEnglishLearningSystem;

import com.swp391.OnlineEnglishLearningSystem.config.StorageProperties;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.UserRole;
import com.swp391.OnlineEnglishLearningSystem.repository.RoleRepository;
import com.swp391.OnlineEnglishLearningSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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
            // Chỉ tạo dữ liệu nếu chưa có

            User admin = userRepository.findByEmail("admin@example.com").orElse(new User());
            admin.setPassword(passwordEncoder.encode("123"));
            User expert = userRepository.findByEmail("expert@example.com").orElse(new User());
            expert.setPassword(passwordEncoder.encode("123"));
            User marketing = userRepository.findByEmail("marketing@example.com").orElse(new User());
            marketing.setPassword(passwordEncoder.encode("123"));
            User user = userRepository.findByEmail("user@example.com").orElse(new User());
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.saveAll(List.of(admin, expert, user, marketing));
            System.out.println(">>> Đã tạo dữ liệu người dùng mẫu!");
        };
    }
}
