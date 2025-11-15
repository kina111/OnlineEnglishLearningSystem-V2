package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Slider;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.model.dto.SliderDTO;
import com.swp391.OnlineEnglishLearningSystem.model.dto.SliderCreateUpdateDto;
import com.swp391.OnlineEnglishLearningSystem.repository.SliderRepository;
import com.swp391.OnlineEnglishLearningSystem.service.SliderService;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SliderServiceImpl implements SliderService {

    private final SliderRepository sliderRepository;
    private final UploadService uploadService;

    private static final String UPLOAD_DIR = "uploads/sliders/";

    public SliderServiceImpl(SliderRepository sliderRepository, UploadService uploadService) {
        this.sliderRepository = sliderRepository;
        this.uploadService = uploadService;
    }

    @Override
    public Page<Slider> getSliders(String keyword, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderNumber").ascending());
        keyword = keyword == null ? "" : keyword;
        if(status!=null) status = status.isBlank()?null:status;
        return sliderRepository.searchSliders(keyword,status, pageable);
    }

    @Override
    public List<Slider> getActiveSliders() {
        return sliderRepository.findByStatusOrderByOrderNumberAsc("SHOW");
    }

    @Override
    public Slider getSliderById(Long id) {
        return sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
    }

    @Override
    public Slider createSlider(SliderCreateUpdateDto dto) {
        try {
            // Lấy user hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            Slider slider = new Slider();
            slider.setUser(currentUser);
            slider.setTitle(dto.getTitle());
            slider.setDescription(dto.getDescription());
            slider.setOrderNumber(dto.getOrderNumber() != null ? dto.getOrderNumber() : 1);
            slider.setStatus(dto.getStatus() != null ? dto.getStatus() : "HIDE");
            slider.setLinkUrl(dto.getLinkUrl());
            slider.setCreatedAt(LocalDateTime.now());
            slider.setUpdatedAt(LocalDateTime.now());

            // Xử lý upload file
            if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
                String imageUrl = saveImageFile(dto.getImageFile());
                slider.setImageUrl(imageUrl);
            }

            return sliderRepository.save(slider);
        } catch (Exception e) {
            throw new RuntimeException("Error creating slider: " + e.getMessage());
        }
    }

    @Override
    public Slider updateSlider(Long id, SliderDTO dto) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
        slider.setTitle(dto.getTitle());
        slider.setDescription(dto.getDescription());
        slider.setOrderNumber(dto.getOrderNumber());
        slider.setStatus(dto.getStatus());
        slider.setImageUrl(dto.getImageUrl());
        slider.setLinkUrl(dto.getLinkUrl());
        slider.setUpdatedAt(LocalDateTime.now());
        return sliderRepository.save(slider);
    }

    @Override
    public Slider updateSliderWithFile(Long id, SliderCreateUpdateDto dto) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
        slider.setTitle(dto.getTitle());
        slider.setDescription(dto.getDescription());
        slider.setOrderNumber(dto.getOrderNumber());
        slider.setOrderNumber(dto.getOrderNumber() != null ? dto.getOrderNumber() : 1);
        slider.setStatus(dto.getStatus() != null ? dto.getStatus() : "HIDE");
        slider.setUpdatedAt(LocalDateTime.now());

        // Xử lý upload file mới nếu có
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String imageUrl = saveImageFile(dto.getImageFile());
            slider.setImageUrl(imageUrl);
        }

        return sliderRepository.save(slider);
    }

    @Override
    public Slider toggleStatus(Long id, String status) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
        slider.setStatus(status);
        slider.setUpdatedAt(LocalDateTime.now());
        return sliderRepository.save(slider);
    }

    @Override
    public void deleteSlider(Long id) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
        sliderRepository.delete(slider);
    }

    @Override
    public void incrementViewCount(Long id) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
        slider.setViewCount(slider.getViewCount() + 1);
        sliderRepository.save(slider);
    }

    @Override
    public Slider save(Slider slider) {
        return sliderRepository.save(slider);
    }

    @Override
    public void approveSlider(Long id) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
        slider.setStatus("SHOW");
        slider.setUpdatedAt(LocalDateTime.now());
        slider.setOrderNumber(getActiveSliders().size() + 1);
        sliderRepository.save(slider);
    }

    @Override
    public void rejectSlider(Long id) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
        slider.setStatus("HIDE");
        slider.setUpdatedAt(LocalDateTime.now());
        sliderRepository.save(slider);
    }

    @Override
    public void takedownSlider(Long id) {
        Slider slider = sliderRepository.findById(id).orElseThrow(() -> new RuntimeException("Slider not found"));
        slider.setStatus("HIDE");
        slider.setOrderNumber(1);
        slider.setUpdatedAt(LocalDateTime.now());
        sliderRepository.save(slider);
        reOrderSliders();
    }


    private void reOrderSliders() {
        List<Slider> sliders = sliderRepository.findByStatusOrderByOrderNumberAsc("SHOW");
        for (int i = 0; i < sliders.size(); i++) {
            sliders.get(i).setOrderNumber(i + 1);
        }
        sliderRepository.saveAll(sliders);
    }

    private String saveImageFile(MultipartFile file) {
        try {
            // Tạo thư mục upload nếu chưa tồn tại
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tạo tên file unique
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("Original filename is null or empty");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Lưu file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/" + UPLOAD_DIR + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Error saving image file: " + e.getMessage());
        }
    }
}
