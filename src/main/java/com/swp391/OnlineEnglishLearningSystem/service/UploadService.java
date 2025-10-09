package com.swp391.OnlineEnglishLearningSystem.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
    String uploadFile(MultipartFile file, String subFolder, String customFilename);
    List<String> uploadMultipleFiles(MultipartFile[] files, String subFolder);
    String uploadImage(MultipartFile image, String subFolder);
    String uploadVideo(MultipartFile video, String subFolder);
    Resource loadFileAsResource(String relativePath);
    boolean deleteFile(String relativePath);
    boolean fileExists(String relativePath);
    String getFileUrl(String relativePath);
}
