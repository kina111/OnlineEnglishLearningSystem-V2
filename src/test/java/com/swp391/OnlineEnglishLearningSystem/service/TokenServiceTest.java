package com.swp391.OnlineEnglishLearningSystem.service;

import com.swp391.OnlineEnglishLearningSystem.model.Token;
import com.swp391.OnlineEnglishLearningSystem.repository.TokenRepository;
import com.swp391.OnlineEnglishLearningSystem.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private Token token;
    private String tokenString = "token-123";

    @BeforeEach
    void setUp() {
        token = new Token();
        token.setToken(tokenString);
        token.setConfirmed_at(null);
        token.setExpired_at(LocalDateTime.now().plusMinutes(10));
    }

    @Test
    void checkValidToke_whenValidToken_shouldReturnToken(){
        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.ofNullable(token));

        //act & assert
        Token t = this.tokenService.checkValidToken(tokenString);
        assertNotNull(t);
        assertSame(token, t);

        verify(tokenRepository, times(1)).findByToken(tokenString);
    }

    @Test
    void checkValidToken_whenTokenNotExists_shouldThrowException(){
        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.empty());

        // --- Act & Assert ---
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tokenService.checkValidToken(tokenString);
        });
        assertEquals("Token does not exist", exception.getMessage());

        // --- Verify ---
        verify(tokenRepository, times(1)).findByToken(tokenString);
    }

    @Test
    void checkValidToken_whenTokenIsConfirmed_shouldThrowException(){
        //arrange
        token.setConfirmed_at(LocalDateTime.now().minusMinutes(10));
        // Mock repository to return the confirmed token
        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.of(token));

        // act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tokenService.checkValidToken(tokenString);
        });
        assertEquals("Token is already confirmed", exception.getMessage());

        // verify
        verify(tokenRepository, times(1)).findByToken(tokenString);
    }

    @Test
    void checkValidToken_whenTokenIsExpired_shouldThrowException(){
        //arrange
        token.setExpired_at(LocalDateTime.now().minusMinutes(10));
        // Mock repository to return the confirmed token
        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.of(token));

        // act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tokenService.checkValidToken(tokenString);
        });
        assertEquals("Token has expired", exception.getMessage());

        // verify
        verify(tokenRepository, times(1)).findByToken(tokenString);
    }
}
