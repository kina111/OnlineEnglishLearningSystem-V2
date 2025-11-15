package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import com.swp391.OnlineEnglishLearningSystem.model.dto.SliderDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.SliderCreateUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SliderService {
    Page<Slider> getSliders(String keyword, String status, int page, int size);
    List<Slider> getActiveSliders();
    Slider getSliderById(Long id);
    Slider createSlider(SliderCreateUpdateDto dto);
    Slider updateSlider(Long id, SliderDTO dto);
    Slider updateSliderWithFile(Long id, SliderCreateUpdateDto dto);
    Slider toggleStatus(Long id, String status);
    void deleteSlider(Long id);
    void incrementViewCount(Long id);
    Slider save(Slider slider);
    void approveSlider(Long id);
    void rejectSlider(Long id);
    void takedownSlider(Long id);
}
