package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Blog;
import com.swp391.OnlineEnglishLearningSystem.model.dto.BlogDTO;
import com.swp391.OnlineEnglishLearningSystem.repository.BlogRepository;
import com.swp391.OnlineEnglishLearningSystem.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Page<BlogDTO> getPaginatedPublishedBlogsByCategorySlug(String blogCategorySlug, Pageable pageable) {
        return this.blogRepository.findAllPublishedByCategory_Slug(blogCategorySlug, pageable);
    }

    @Override
    public BlogDTO getBlogById(Long blogId) {
        return this.blogRepository.findPublishedBlogById(blogId);
    }

    @Override
    public List<BlogDTO> findLatestBlogs(int i) {
        return this.blogRepository.findLatestPublishedBlogs(PageRequest.of(0, i));
    }
}
