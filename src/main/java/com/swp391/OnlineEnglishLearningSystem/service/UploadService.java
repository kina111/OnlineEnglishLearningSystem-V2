package com.swp391.OnlineEnglishLearningSystem.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
    /**
     * Upload file và trả về tên file đã lưu
     */
    String uploadFile(MultipartFile file);

    /**
     * Upload file với custom filename
     */
    String uploadFile(MultipartFile file, String customFilename);

    /**
     * Upload multiple files
     */
    List<String> uploadMultipleFiles(MultipartFile[] files);

    /**
     * Upload image với validation
     */
    String uploadImage(MultipartFile image);

    /**
     * Xóa file
     */
    boolean deleteFile(String filename);

    /**
     * Lấy file dưới dạng Resource
     */
    Resource loadFileAsResource(String filename);

    /**
     * Kiểm tra file có tồn tại không
     */
    boolean fileExists(String filename);

    /**
     * Lấy URL của file
     */
    String getFileUrl(String filename);
}
