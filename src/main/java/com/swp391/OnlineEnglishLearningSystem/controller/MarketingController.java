package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.service.SliderService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/marketing")
public class MarketingController {

    @Autowired
    private SliderService sliderService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String marketingDashboard(Model model, Principal principal) {
        try {
            // Lấy thông tin user hiện tại
            User currentUser = userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow();
            model.addAttribute("currentUser", currentUser);

            // Lấy danh sách sliders để quản lý (lấy tất cả, không phân trang)
            Page<Slider> sliderPage = sliderService.getSliders("", null, 0, 1000);
            List<Slider> sliders = sliderPage.getContent();
            model.addAttribute("sliders", sliders);

            // Thống kê cơ bản
            long totalSliders = sliders.size();
            long activeSliders = sliders.stream().filter(s -> s.getStatus().equals("SHOW")).count();

            model.addAttribute("totalSliders", totalSliders);
            model.addAttribute("activeSliders", activeSliders);

            return "marketing/dashboard";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/sliders")
    public String manageSliders(Model model, Principal principal) {
        try {
            User currentUser = userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow();
            model.addAttribute("currentUser", currentUser);

            Page<Slider> sliderPage = sliderService.getSliders("", null, 0, 1000);
            List<Slider> sliders = sliderPage.getContent();
            model.addAttribute("sliders", sliders);

            return "marketing/slider-management";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }
}
