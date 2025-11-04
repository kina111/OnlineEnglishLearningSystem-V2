package com.swp391.OnlineEnglishLearningSystem.repository;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Long> {

    // Tìm kiếm theo tiêu đề hoặc linkUrl
    @Query("SELECT s FROM Slider s WHERE " +
            "(LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.linkUrl) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:status IS NULL OR s.status = :status)")
    Page<Slider> searchSliders(@Param("keyword") String keyword,
                               @Param("status") String status,
                               Pageable pageable);

    // Lấy slider theo status và sắp xếp theo orderNumber
    List<Slider> findByStatusOrderByOrderNumberAsc(String status);
}


