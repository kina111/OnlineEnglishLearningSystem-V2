package com.swp391.OnlineEnglishLearningSystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
    private String location = "uploads";
    private long maxFileSize = 5 * 1024 * 1024; // 5MB default
    private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx"};
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
}