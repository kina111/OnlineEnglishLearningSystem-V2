package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Blog;
import com.swp391.OnlineEnglishLearningSystem.model.dto.BlogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {
    Page<BlogDTO> getPaginatedPublishedBlogsByCategorySlug(String blogCategorySlug, Pageable pageable);

    BlogDTO getBlogById(Long blogId);

    List<BlogDTO> findLatestBlogs(int quantity);
}
