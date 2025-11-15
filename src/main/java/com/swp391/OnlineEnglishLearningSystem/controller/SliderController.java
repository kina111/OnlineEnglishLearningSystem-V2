package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import com.swp391.OnlineEnglishLearningSystem.model.dto.SliderDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.SliderCreateUpdateDto;
import com.swp391.OnlineEnglishLearningSystem.service.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Controller
@RequestMapping("/admin/sliders")
public class SliderController {

    @Autowired
    private SliderService sliderService;

    // Hiển thị danh sách slider (Admin UI)
    @GetMapping("")
    public String getSliders(
            @RequestParam(required = false,defaultValue = "") String keyword,
            @RequestParam(required = false,defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<Slider> sliderPage = sliderService.getSliders(keyword, status, page, size);
        model.addAttribute("sliderPage", sliderPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);

        // Tính toán số trang để hiển thị
        int totalPages = sliderPage.getTotalPages();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "admin/slider/list";
    }


    @GetMapping("/approve/{sliderId}")
    public String approveSlider(@PathVariable Long sliderId){
        Slider slider = sliderService.getSliderById(sliderId);
        if(slider!=null&&slider.getStatus().equals("PENDING")){
            sliderService.approveSlider(sliderId);
        }
        return "redirect:/admin/sliders";
    }
    @GetMapping("/reject/{sliderId}")
    public String rejectSlider(@PathVariable Long sliderId){
        Slider slider = sliderService.getSliderById(sliderId);
        if(slider!=null&&slider.getStatus().equals("PENDING")){
            sliderService.rejectSlider(sliderId);
        }
        return "redirect:/admin/sliders";
    }

    @GetMapping("/takeDown/{sliderId}")
    public String takeDownSlider(@PathVariable Long sliderId){
        Slider slider = sliderService.getSliderById(sliderId);
        if(slider!=null&&slider.getStatus().equals("SHOW")){
            sliderService.takedownSlider(sliderId);
        }
        return "redirect:/admin/sliders";
    }

    // Xem tất cả slider (public view)
    @GetMapping("/view-all")
    public String viewAllSliders(Model model) {
        List<Slider> activeSliders = sliderService.getActiveSliders();
        model.addAttribute("sliders", activeSliders);
        return "slider/view-all";
    }

}
