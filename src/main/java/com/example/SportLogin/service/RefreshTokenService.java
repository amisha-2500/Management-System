package com.example.SportLogin.service;

import com.example.SportLogin.dto.LoginDto;
import com.example.SportLogin.exception.TokenRefreshException;
import com.example.SportLogin.dto.RefreshToken;
import com.example.SportLogin.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RefreshTokenService {

    private final Long refreshTokenDurationMs = (long) (8 * 60 * 60 * 1000);

    private final Map<String, RefreshToken> refreshTokenStore = new ConcurrentHashMap<>();

    public RefreshToken findByToken(String token) {
        return refreshTokenStore.get(token);
    }

    public RefreshToken createRefreshToken(LoginDto loginDto) {
        RefreshToken refreshToken = new RefreshToken();
        String username = loginDto.getUsername();

        refreshToken.setUser(new User());

        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshTokenStore.put(refreshToken.getToken(), refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenStore.remove(token.getToken());
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }
}