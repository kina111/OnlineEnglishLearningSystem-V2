package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import com.swp391.OnlineEnglishLearningSystem.service.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PublicSliderController {

    @Autowired
    private SliderService sliderService;

    // Hiển thị slider cho trang chủ
    @GetMapping("/sliders")
    public String getPublicSliders(Model model) {
        // Lấy tất cả slider có status = SHOW, sắp xếp theo orderNumber
        List<Slider> sliders = sliderService.getActiveSliders();
        model.addAttribute("sliders", sliders);
        return "components/slider";
    }

    // Trang demo slider
    @GetMapping("/slider-demo")
    public String sliderDemo(Model model) {
        List<Slider> sliders = sliderService.getActiveSliders();
        model.addAttribute("sliders", sliders);
        return "slider-demo";
    }

//    // API endpoint để lấy slider data cho AJAX
//    @GetMapping("/api/public/sliders")
//    public org.springframework.http.ResponseEntity<List<Slider>> getPublicSlidersApi() {
//        List<Slider> sliders = sliderService.getActiveSliders();
//        return org.springframework.http.ResponseEntity.ok(sliders);
//    }
}

