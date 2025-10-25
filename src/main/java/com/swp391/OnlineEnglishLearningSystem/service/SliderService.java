package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import com.swp391.OnlineEnglishLearningSystem.model.dto.SliderDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.SliderCreateUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SliderService {
    Page<Slider> getSliders(String keyword, String status, int page, int size);
    List<Slider> getActiveSliders();
    Slider getSliderById(Integer id);
    Slider createSlider(SliderCreateUpdateDto dto);
    Slider updateSlider(Integer id, SliderDTO dto);
    Slider updateSliderWithFile(Integer id, SliderCreateUpdateDto dto);
    Slider toggleStatus(Integer id, String status);
    void deleteSlider(Integer id);
    void incrementViewCount(Integer id);
}
