package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.BlogCategory;
import com.swp391.OnlineEnglishLearningSystem.repository.BlogCategoryRepository;
import com.swp391.OnlineEnglishLearningSystem.service.BlogCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogCategoryServiceImpl implements BlogCategoryService {
    private final BlogCategoryRepository blogCategoryRepository;

    @Override
    public List<BlogCategory> findAll() {
        return this.blogCategoryRepository.findAll();
    }
}
