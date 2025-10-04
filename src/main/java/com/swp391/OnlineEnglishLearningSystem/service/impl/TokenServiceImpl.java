package com.swp391.OnlineEnglishLearningSystem.service.impl;

import com.swp391.OnlineEnglishLearningSystem.model.Token;
import com.swp391.OnlineEnglishLearningSystem.model.User;
import com.swp391.OnlineEnglishLearningSystem.repository.TokenRepository;
import com.swp391.OnlineEnglishLearningSystem.service.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token create(User newUser) {
        return new Token(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES),
                newUser
        );
    }

    @Override
    public void save(Token newToken) {
        this.tokenRepository.save(newToken);
    }

    @Override
    public Token checkValidToken(String tokenValue) {
        Token token = tokenRepository.findByToken(String.valueOf(tokenValue))
                .orElseThrow(() -> new IllegalArgumentException("Token does not exist"));

        if (token.getConfirmed_at() != null) {
            throw new IllegalArgumentException("Token is already confirmed");
        }
        if (LocalDateTime.now().isAfter(token.getExpired_at())) {
            throw new IllegalArgumentException("Token has expired");
        }
        return token;
    }
}
