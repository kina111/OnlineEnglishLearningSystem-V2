package com.swp391.OnlineEnglishLearningSystem.model.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long fromUser;

    private String userName;

    private Long toGroup;

    private String content;

    private String userAvatarUrl;

    private String fileUrl;

    private LocalDateTime timestamp = LocalDateTime.now();

    public MessageDTO() {
    }

    public MessageDTO(Long fromUser, Long toGroup, String content, LocalDateTime timestamp) {
        this.fromUser = fromUser;
        this.toGroup = toGroup;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getFromUser() {
        return fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    public Long getToGroup() {
        return toGroup;
    }

    public void setToGroup(Long toGroup) {
        this.toGroup = toGroup;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
