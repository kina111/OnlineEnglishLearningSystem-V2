package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {

    public enum ChatType {
        PRIVATE, GROUP
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType type;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name; // chỉ dùng cho nhóm

    @Column(name = "description", columnDefinition = "NVARCHAR(1000)")
    private String description; // mô tả nhóm

    private String avatar; // ảnh nhóm (nếu có)

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Message> messages = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    public Chat(Long id, ChatType type, String name, String description, String avatar,
                List<ChatMember> members, List<Message> messages, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.avatar = avatar;
        this.members = members;
        this.messages = messages;
        this.createdAt = createdAt;
    }
}
