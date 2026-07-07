package com.swp391.OnlineEnglishLearningSystem.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageDTO {
    private Long fromUser;
    private String userName;
    private Long toGroup;
    private String content;
    private String userAvatarUrl;
    private String fileUrl;
    private LocalDateTime timestamp = LocalDateTime.now();

    public MessageDTO(Long fromUser, Long toGroup, String content, LocalDateTime timestamp) {
        this.fromUser = fromUser;
        this.toGroup = toGroup;
        this.content = content;
        this.timestamp = timestamp;
    }
}
