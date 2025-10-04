package com.swp391.OnlineEnglishLearningSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Cho phép tất cả không cần auth
                )
                .csrf(csrf -> csrf.disable()); // Tắt CSRF cho POST requests

        return http.build();
    }

    // Optional: Tắt authentication manager
    // @Bean
    // public AuthenticationManager authenticationManager() {
    //     return authentication -> { throw new RuntimeException("Authentication disabled"); };
    // }
}
