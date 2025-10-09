package com.swp391.OnlineEnglishLearningSystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
    private String location = "uploads";
    private long maxFileSize = 5 * 1024 * 1024;
    private long maxVideoFileSize = 500 * 1024 * 1024;// 500MB default
    private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx"};
    private String[] allowedVideoExtensions = {"mp4", "avi", "mkv", "mov", "webm"};
    private boolean autoCreateDir = true;

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public long getMaxFileSize() { return maxFileSize; }
    public void setMaxFileSize(long maxFileSize) { this.maxFileSize = maxFileSize; }

    public String[] getAllowedExtensions() { return allowedExtensions; }
    public void setAllowedExtensions(String[] allowedExtensions) { this.allowedExtensions = allowedExtensions; }

    public boolean isAutoCreateDir() { return autoCreateDir; }
    public void setAutoCreateDir(boolean autoCreateDir) { this.autoCreateDir = autoCreateDir; }

    public long getMaxVideoFileSize() {
        return maxVideoFileSize;
    }

    public void setMaxVideoFileSize(long maxVideoFileSize) {
        this.maxVideoFileSize = maxVideoFileSize;
    }

    public String[] getAllowedVideoExtensions() {
        return allowedVideoExtensions;
    }

    public void setAllowedVideoExtensions(String[] allowedVideoExtensions) {
        this.allowedVideoExtensions = allowedVideoExtensions;
    }
}