package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.Blog;
import com.swp391.OnlineEnglishLearningSystem.model.dto.BlogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("""
        Select new com.swp391.OnlineEnglishLearningSystem.model.dto.BlogDTO(
            b.id,
            a.avatar,
            a.fullName,
            b.title,
            b.shortDescription,
            b.content,
            b.createdAt,
            b.updatedAt,
            b.thumbnail,
            bc.name,
            bc.slug
        ) from Blog b
        join b.blogCategory bc
        join b.author a
        where (:slug is null or bc.slug = :slug or :slug = '') and b.status = 'PUBLISHED'
""")
    Page<BlogDTO> findAllPublishedByCategory_Slug(@Param("slug") String slug, Pageable pageable);

    @Query("""
        Select new com.swp391.OnlineEnglishLearningSystem.model.dto.BlogDTO(
            b.id,
            a.avatar,
            a.fullName,
            b.title,
            b.shortDescription,
            b.content,
            b.createdAt,
            b.updatedAt,
            b.thumbnail,
            bc.name,
            bc.slug
        ) from Blog b
        join b.blogCategory bc
        join b.author a
        where b.id = :id and b.status = 'PUBLISHED'
""")
    BlogDTO findPublishedBlogById(@Param("id") Long id);

    @Query("""
        Select new com.swp391.OnlineEnglishLearningSystem.model.dto.BlogDTO(
            b.id,
            a.avatar,
            a.fullName,
            b.title,
            b.shortDescription,
            b.content,
            b.createdAt,
            b.updatedAt,
            b.thumbnail,
            bc.name,
            bc.slug
        ) from Blog b
        join b.blogCategory bc
        join b.author a
        where b.status = 'PUBLISHED'
    """)
    List<BlogDTO> findLatestPublishedBlogs(Pageable pageable);
}
