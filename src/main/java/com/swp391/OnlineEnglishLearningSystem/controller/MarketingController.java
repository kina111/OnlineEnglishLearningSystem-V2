package com.swp391.OnlineEnglishLearningSystem.controller;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.service.SliderService;
import com.swp391.OnlineEnglishLearningSystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/marketing")
public class MarketingController {

    private final SliderService sliderService;

    private final UserService userService;

    public MarketingController(SliderService sliderService, UserService userService) {
        this.sliderService = sliderService;
        this.userService = userService;
    }

    @GetMapping("")
    public String marketingDashboard(Model model, Principal principal, HttpSession session) {
        try {
            // Lấy thông tin user hiện tại
//            User currentUser = userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow();
            Long userId = (Long) session.getAttribute("currentUserId");
            User currentUser = userService.getUserById(userId);
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
    public String manageSliders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model, 
            Principal principal,
            HttpSession session) {
        try {
//            User currentUser = userService.findByEmailAndEnabledTrue(principal.getName()).orElseThrow();
            Long userId = (Long) session.getAttribute("currentUserId");
            User currentUser = userService.getUserById(userId);
            model.addAttribute("currentUser", currentUser);

            Page<Slider> sliderPage = sliderService.getSliders(keyword, status, page, size);
            model.addAttribute("sliderPage", sliderPage);
            model.addAttribute("keyword", keyword != null ? keyword : "");
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

            return "marketing/slider-management";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/sliders/delete/{sliderId}")
    public String deleteSlider(@PathVariable("sliderId") Long sliderId, Model model, Principal principal, HttpSession session) {
        Slider slider = sliderService.getSliderById(sliderId);
        Long userId = (Long) session.getAttribute("currentUserId");
        User currentUser = userService.getUserById(userId);
        if(slider != null&&currentUser!=null&&(currentUser.getId() == slider.getUser().getId()||currentUser.getRole().equals("ADMIN"))){
            sliderService.deleteSlider(sliderId);
        }
        return "redirect:/marketing/sliders";
    }

}
