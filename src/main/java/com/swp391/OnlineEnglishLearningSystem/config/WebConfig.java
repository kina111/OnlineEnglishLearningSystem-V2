package com.swp391.OnlineEnglishLearningSystem.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose folder uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // đường dẫn tới folder uploads
    }
}
