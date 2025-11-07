package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.BlogCategory;
import com.swp391.OnlineEnglishLearningSystem.repository.BlogCategoryRepository;
import com.swp391.OnlineEnglishLearningSystem.service.BlogCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;
    @Override
    public List<BlogCategory> findAll() {
        return this.blogCategoryRepository.findAll();
    }
}
