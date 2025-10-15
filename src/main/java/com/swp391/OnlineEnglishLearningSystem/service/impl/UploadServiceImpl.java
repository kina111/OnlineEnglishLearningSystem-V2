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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UploadServiceImpl implements UploadService {

    private final Path rootStorageLocation;//thư mục gốc
    private final StorageProperties storageProperties;

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp");
    private static final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of("mp4", "avi", "mkv", "mov", "webm");
    private static final Set<String> ALLOWED_AUDIO_EXTENSIONS = Set.of("mp3", "aac", "wav", "ogg");

    @Autowired
    public UploadServiceImpl(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.rootStorageLocation = Paths.get(storageProperties.getLocation()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String subFolder, String customFilename) {
        String subFolderPath = StringUtils.cleanPath(Objects.requireNonNullElse(subFolder, ""));
        if (subFolderPath.contains("..")) {
            throw new RuntimeException("Cannot store file with relative path outside current directory");
        }
        try{
            String fileName = generateSafeFileName(file, customFilename);
            Path targetDirectory = this.rootStorageLocation.resolve(subFolderPath);
            Files.createDirectories(targetDirectory);
            Path targetLocation = targetDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            //trả về đường dẫn tương đối (subfolder/filename)
            return Paths.get(subFolderPath).resolve(fileName).toString().replace("\\", "/");
        }catch (IOException e){
            throw new RuntimeException("Could not store file. Please try again!", e);
        }
    }

    @Override
    public List<String> uploadMultipleFiles(MultipartFile[] files, String subFolder) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file, subFolder, null))
                .collect(Collectors.toList());
    }

    @Override
    public String uploadImage(MultipartFile image, String subFolder) {
        validateFileIsImage(image);
        validateFile(image, storageProperties.getMaxFileSize(), null);
        return uploadFile(image, subFolder, null);
    }

    @Override
    public String uploadVideo(MultipartFile video, String subFolder) {
        validateFileIsVideo(video);
        validateFile(video, storageProperties.getMaxVideoFileSize(), null);
        return uploadFile(video, subFolder, null);
    }

    public String uploadAudio(MultipartFile audio, String subFolder) {
        validateFileIsAudio(audio);
        validateFile(audio, storageProperties.getMaxAudioFileSize(), null);
        return uploadFile(audio, subFolder, null);
    }

    @Override
    public Resource loadFileAsResource(String relativePath) {
        try {
            Path filePath = this.rootStorageLocation.resolve(relativePath).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + relativePath);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + relativePath, e);
        }
    }

    @Override
    public boolean deleteFile(String relativePath) {
        try{
            if (relativePath == null || relativePath.trim().isEmpty()) {
                return false;
            }
            Path filePath = this.rootStorageLocation.resolve(relativePath).normalize();
            return Files.deleteIfExists(filePath);
        }catch (Exception e){
            throw new RuntimeException("Could not delete file: " + relativePath, e);
        }
    }

    @Override
    public boolean fileExists(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            return false;
        }
        try{
            Path filePath = this.rootStorageLocation.resolve(relativePath).normalize();
            return Files.exists(filePath);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public String getFileUrl(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            return null;
        }
        return "/uploads/" + relativePath;
    }

    private void validateFile(MultipartFile file, long maxSize, String[] allowedExtensions){
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size exceeds maximum allowed size of " + maxSize + " bytes.");
        }
        if (Objects.requireNonNull(file.getOriginalFilename().contains(".."))){
            throw new RuntimeException("File name contains invalid path sequence.");
        }

        if (allowedExtensions != null && allowedExtensions.length > 0) {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!isExtensionInList(fileExtension, allowedExtensions)){
                throw new RuntimeException("File extension is not allowed.");
            }
        }
    }

    private void validateFileIsImage(MultipartFile file){
        String ext = getFileExtension(file.getOriginalFilename()).replace(".", "");
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(ext.toLowerCase())){
            throw new RuntimeException("File is not an image. Allowed extensions: " + ALLOWED_IMAGE_EXTENSIONS.toString());
        }
    }

    private void validateFileIsVideo(MultipartFile file){
        String ext = getFileExtension(file.getOriginalFilename()).replace(".", "");
        if (!ALLOWED_VIDEO_EXTENSIONS.contains(ext.toLowerCase())){
            throw new RuntimeException("File is not a video. Allowed extensions: " + ALLOWED_VIDEO_EXTENSIONS.toString());
        }
    }

    private void validateFileIsAudio(MultipartFile file){
        String ext = getFileExtension(file.getOriginalFilename()).replace(".", "");
        if (!ALLOWED_AUDIO_EXTENSIONS.contains(ext.toLowerCase())){
            throw new RuntimeException("File is not an audio. Allowed extensions: " + ALLOWED_AUDIO_EXTENSIONS.toString());
        }
    }

    private String generateSafeFileName(MultipartFile file, String customFilename){
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getFileExtension(originalFileName);
        if (customFilename != null && !customFilename.trim().isEmpty()) {
            return StringUtils.cleanPath(customFilename + fileExtension);
        }else{
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String baseNamee = originalFileName.replace(fileExtension, "");
            return baseNamee + "_" + timeStamp + fileExtension;
        }
    }

    private String getFileExtension(String fileName){
        return fileName != null && fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf(".")).toLowerCase()
                : "";
    }

    private boolean isExtensionInList(String extension, String[] extensions){
        if (extension.isEmpty() || extensions == null || extensions.length == 0) {
            return false;
        }
        String exWithoutDot = extension.substring(1);
        return Arrays.asList(extensions).contains(exWithoutDot.toLowerCase());
    }
}
