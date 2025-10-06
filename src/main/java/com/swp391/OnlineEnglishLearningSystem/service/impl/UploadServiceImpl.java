package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.config.StorageProperties;
import com.swp391.OnlineEnglishLearningSystem.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UploadServiceImpl implements UploadService {
    private final Path fileStorageLocation;
    private final StorageProperties storageProperties;

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp");
    private static final Set<String> ALLOWED_DOCUMENT_EXTENSIONS = Set.of("pdf", "doc", "docx", "txt", "xls", "xlsx");

    @Autowired
    public UploadServiceImpl(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.fileStorageLocation = Paths.get(storageProperties.getLocation())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, null);
    }

    @Override
    public String uploadFile(MultipartFile file, String customFilename) {
        // Validate file
        validateFile(file);

        try {
            // Generate safe filename
            String fileName = generateSafeFileName(file, customFilename);

            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }

    @Override
    public List<String> uploadMultipleFiles(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @Override
    public String uploadImage(MultipartFile image) {
        // Validate it's actually an image
        if (!isImage(image)) {
            throw new RuntimeException("File is not a valid image. Allowed formats: " + ALLOWED_IMAGE_EXTENSIONS);
        }

        // Validate image size
        if (image.getSize() > storageProperties.getMaxFileSize()) {
            throw new RuntimeException("Image size exceeds maximum allowed size: " +
                    storageProperties.getMaxFileSize() / (1024 * 1024) + "MB");
        }

        return uploadFile(image);
    }

    @Override
    public boolean deleteFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file: " + filename, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found: " + filename, ex);
        }
    }

    @Override
    public boolean fileExists(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            return Files.exists(filePath);
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public String getFileUrl(String filename) {
        return "/uploads/" + filename; // URL để truy cập file qua controller
    }

    // ============ PRIVATE METHODS ============

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Check file size
        if (file.getSize() > storageProperties.getMaxFileSize()) {
            throw new RuntimeException("File size exceeds maximum allowed size: " +
                    storageProperties.getMaxFileSize() / (1024 * 1024) + "MB");
        }

        // Check file extension
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!isAllowedExtension(fileExtension)) {
            throw new RuntimeException("File type not allowed. Allowed types: " +
                    String.join(", ", storageProperties.getAllowedExtensions()));
        }

        // Security check for path traversal
        if (file.getOriginalFilename().contains("..")) {
            throw new RuntimeException("Filename contains invalid path sequence: " + file.getOriginalFilename());
        }
    }

    private String generateSafeFileName(MultipartFile file, String customFilename) {
        String fileExtension = getFileExtension(file.getOriginalFilename());

        if (customFilename != null && !customFilename.trim().isEmpty()) {
            return StringUtils.cleanPath(customFilename + fileExtension);
        } else {
            // Generate unique filename: timestamp + random + extension
            String timestamp = String.valueOf(System.currentTimeMillis());
            String random = String.valueOf((int) (Math.random() * 1000));
            return timestamp + "_" + random + fileExtension;
        }
    }

    private String getFileExtension(String filename) {
        return filename != null && filename.contains(".")
                ? filename.substring(filename.lastIndexOf(".")).toLowerCase()
                : "";
    }

    private boolean isAllowedExtension(String fileExtension) {
        if (fileExtension.isEmpty()) return false;

        String extensionWithoutDot = fileExtension.substring(1); // Remove the dot
        return Arrays.stream(storageProperties.getAllowedExtensions())
                .anyMatch(ext -> ext.equalsIgnoreCase(extensionWithoutDot));
    }

    private boolean isImage(MultipartFile file) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String extensionWithoutDot = fileExtension.substring(1);
        return ALLOWED_IMAGE_EXTENSIONS.contains(extensionWithoutDot.toLowerCase());
    }

    /**
     * Get storage location (useful for other services)
     */
    public Path getStorageLocation() {
        return fileStorageLocation;
    }
}
