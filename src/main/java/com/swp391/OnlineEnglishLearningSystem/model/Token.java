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
@Table(name = "confirmation_tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expired_at;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmed_at;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Token(String token, LocalDateTime created_at, LocalDateTime expired_at) {
        this.token = token;
        this.created_at = created_at;
        this.expired_at = expired_at;
    }

    public Token(String token, LocalDateTime created_at, LocalDateTime expired_at, User user) {
        this.token = token;
        this.created_at = created_at;
        this.expired_at = expired_at;
        this.user = user;
    }
}
