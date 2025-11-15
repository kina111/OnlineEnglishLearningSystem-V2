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
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
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

    // Hiển thị form tạo slider mới
    @GetMapping("/create")
    public String createSliderForm(Model model) {
        model.addAttribute("slider", new SliderCreateUpdateDto());
        return "admin/slider/create";
    }

    // Xử lý tạo slider mới
    @PostMapping("/create")
    public String createSlider(@ModelAttribute SliderCreateUpdateDto dto, RedirectAttributes redirectAttributes) {
        try {
            sliderService.createSlider(dto);
            redirectAttributes.addFlashAttribute("success", "Tạo slider thành công!");

            // Redirect về dashboard phù hợp dựa trên role
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_MARKETING"))) {
                return "redirect:/marketing/sliders";
            } else {
                return "redirect:/admin/sliders";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo slider: " + e.getMessage());
            return "redirect:/admin/sliders/create";
        }
    }

    // Hiển thị form chỉnh sửa slider
    @GetMapping("/update/{id}")
    public String updateSliderForm(@PathVariable Long id, Model model) {
        try {
            Slider slider = sliderService.getSliderById(id);
            SliderCreateUpdateDto dto = new SliderCreateUpdateDto();
            dto.setTitle(slider.getTitle());
            dto.setDescription(slider.getDescription());
            dto.setOrderNumber(slider.getOrderNumber());
            dto.setStatus(slider.getStatus());
            dto.setLinkUrl(slider.getLinkUrl());

            model.addAttribute("slider", dto);
            model.addAttribute("sliderId", id);
            model.addAttribute("currentImageUrl", slider.getImageUrl());
            return "admin/slider/update";
        } catch (Exception e) {
            return "redirect:/admin/sliders?error=" + e.getMessage();
        }
    }

    // Xử lý cập nhật slider
    @PostMapping("/update/{id}")
    public String updateSlider(@PathVariable Long id, @ModelAttribute SliderCreateUpdateDto dto, RedirectAttributes redirectAttributes) {
        try {
            sliderService.updateSliderWithFile(id, dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật slider thành công!");

            // Redirect về dashboard phù hợp dựa trên role
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_MARKETING"))) {
                return "redirect:/marketing/sliders";
            } else {
                return "redirect:/admin/sliders";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật slider: " + e.getMessage());
            return "redirect:/admin/sliders/update/" + id;
        }
    }

    // Xóa slider
    @PostMapping("/delete/{id}")
    public String deleteSlider(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sliderService.deleteSlider(id);
            redirectAttributes.addFlashAttribute("success", "Xóa slider thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa slider: " + e.getMessage());
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

    // API endpoints cho AJAX calls
    @RestController
    @RequestMapping("/api/sliders")
    public static class SliderApiController {

        @Autowired
        private SliderService sliderService;

        // Lấy danh sách slider có phân trang, lọc, tìm kiếm
        @GetMapping
        public ResponseEntity<Page<Slider>> getSliders(
                @RequestParam(required = false) String keyword,
                @RequestParam(required = false) String status,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            return ResponseEntity.ok(sliderService.getSliders(keyword, status, page, size));
        }

        // Lấy slider theo ID
        @GetMapping("/{id}")
        public ResponseEntity<Slider> getSliderById(@PathVariable Long id) {
            return ResponseEntity.ok(sliderService.getSliderById(id));
        }

        // Chỉnh sửa slider
        @PutMapping("/{id}")
        public ResponseEntity<Slider> updateSlider(@PathVariable Long id, @RequestBody SliderDTO dto) {
            return ResponseEntity.ok(sliderService.updateSlider(id, dto));
        }

        // Ẩn/Hiện slider
        @PatchMapping("/{id}/status")
        public ResponseEntity<Slider> toggleStatus(@PathVariable Long id, @RequestParam String status) {
            return ResponseEntity.ok(sliderService.toggleStatus(id, status));
        }

        // Tăng lượt xem slider
        @PostMapping("/{id}/view")
        public ResponseEntity<Void> incrementViewCount(@PathVariable Long id) {
            sliderService.incrementViewCount(id);
            return ResponseEntity.ok().build();
        }
    }
}
