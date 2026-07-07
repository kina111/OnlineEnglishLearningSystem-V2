package com.swp391.OnlineEnglishLearningSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false, columnDefinition = "NVARCHAR(2000)")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String fileURl;

    public Message(Long id, Chat chat, User sender, String content, LocalDateTime createdAt, String fileURl) {
        this.id = id;
        this.chat = chat;
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt;
        this.fileURl = fileURl;
    }
}
