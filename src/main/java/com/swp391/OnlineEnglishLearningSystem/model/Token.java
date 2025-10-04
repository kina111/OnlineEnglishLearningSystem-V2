package com.swp391.OnlineEnglishLearningSystem.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token",
            nullable = false)
    private String token;

    @Column(name = "created_at",
            nullable = false)
    private LocalDateTime created_at;

    @Column(name = "expired_at",
            nullable = false)
    private LocalDateTime expired_at;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmed_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    public Token() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(LocalDateTime expired_at) {
        this.expired_at = expired_at;
    }

    public LocalDateTime getConfirmed_at() {
        return confirmed_at;
    }

    public void setConfirmed_at(LocalDateTime confirmed_at) {
        this.confirmed_at = confirmed_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
