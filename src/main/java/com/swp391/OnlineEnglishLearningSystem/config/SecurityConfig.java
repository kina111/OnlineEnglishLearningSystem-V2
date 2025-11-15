package com.swp391.OnlineEnglishLearningSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userService;

    public SecurityConfig(CustomUserDetailsService userService) {
        this.userService = userService;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/home",
                                "/courses/learner","/courses/{id}/learner",
                                "/blogs", "/blogs/{id}",
                                "/css/**", "/js/**",
                                "/uploads/**",
                                "/chats").permitAll()
                        .requestMatchers("/marketing/**").hasRole("MARKETING")
                        // --- 2. QUYỀN CỦA EXPERT (Các rule chỉ Expert có) ---
                        .requestMatchers(HttpMethod.POST, "/courses/{id}/submit-review").hasRole("EXPERT")

                        // Gom nhóm các API của Expert
                        .requestMatchers(HttpMethod.PUT,
                                "/api/chapters/{chapterId}",
                                "/api/lectures/{lectureId}",
                                "/api/quizzes/{quizId}"
                        ).hasRole("EXPERT")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/courses/{courseId}/chapters/{chapterId}",
                                "/api/chapters/{chapterId}/lessons/{id}"
                        ).hasRole("EXPERT")

                        .requestMatchers(HttpMethod.POST,
                                "/api/courses/{courseId}/chapters",
                                "/api/chapters/{chapterId}/lectures",
                                "/api/chapters/{chapterId}/quizzes"
                        ).hasRole("EXPERT")

                        // Gom nhóm các đường dẫn tạo/cập nhật Question
                        .requestMatchers(
                                "/courses/{courseId}/quizzes/{quizId}/questions/multiple-choice/create",
                                "/courses/{courseId}/quizzes/{quizId}/questions/short-answer/create",
                                "/courses/{courseId}/quizzes/{quizId}/questions/{questionId}/delete",
                                "/courses/{courseId}/quizzes/{quizId}/questions/{questionId}/multiple-choice/update",
                                "/courses/{courseId}/quizzes/{quizId}/questions/{questionId}/short-answer/update"
                        ).hasRole("EXPERT")

                        // --- 3. QUYỀN CHUNG (Cả ADMIN và EXPERT đều có) ---
                        .requestMatchers(HttpMethod.GET,
                                "/courses/{id}",
                                "/courses/create",
                                "/courses/{id}/update",
                                "/courses/users/{id}",
                                "/courses/{courseId}/quizzes/{quizId}/questions/**"
                        ).hasAnyRole("ADMIN", "EXPERT")

                        .requestMatchers(HttpMethod.POST,
                                "/courses/create",
                                "/courses/{id}/update",
                                "/courses/{id}/delete"
                        ).hasAnyRole("ADMIN", "EXPERT")

                        // --- QUYỀN CỦA ADMIN (Ưu tiên các rule cụ thể nhất) ---
                        .requestMatchers("/quiz/*").hasRole("ADMIN")
                        .requestMatchers("/admin/**", "/courses/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/courses/{courseId}").hasRole("ADMIN") // Change status
                        .requestMatchers("/api/{courseId}/toggle-featured").hasRole("ADMIN") // Giả sử là POST/PUT, nếu rõ method thì nên ghi

                        // Tất cả các request còn lại đều cần phải xác thực
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")   // chỗ này phải đúng action form
                        .defaultSuccessUrl("/", true)
                        .successHandler(myAuthenticationSuccessHandler())// true = luôn luôn redirect về /home
                        .failureUrl("/login?error=true")
                        .permitAll()
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
