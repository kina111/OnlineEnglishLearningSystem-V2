package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import com.swp391.OnlineEnglishLearningSystem.service.SliderService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sliders")
@RequiredArgsConstructor
public class PublicSliderController {

    private final SliderService sliderService;

    // Hiển thị slider cho trang chủ
    @GetMapping("")
    public String getPublicSliders(Model model) {
        // Lấy tất cả slider có status = SHOW, sắp xếp theo orderNumber
        List<Slider> sliders = sliderService.getActiveSliders();
        model.addAttribute("sliders", sliders);
        return "components/slider";
    }

    // Trang demo slider
    @GetMapping("/demo")
    public String sliderDemo(Model model) {
        List<Slider> sliders = sliderService.getActiveSliders();
        model.addAttribute("sliders", sliders);
        return "slider-demo";
    }

    @GetMapping("/view/{id}")
    public String getSliderById(@PathVariable("id") Long id) {
        Slider slider = sliderService.getSliderById(id);
        if (slider != null) {
            sliderService.incrementViewCount(id);
            if(slider.getLinkUrl()!=null&&!slider.getLinkUrl().isBlank()){
                return "redirect:" + slider.getLinkUrl();
            }
        }
        return "redirect:/";
    }
}

