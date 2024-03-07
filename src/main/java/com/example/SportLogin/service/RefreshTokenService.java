package com.example.SportLogin.service;

import com.example.SportLogin.dto.LoginDto;
import com.example.SportLogin.dto.TokenRefreshRequest;
import com.example.SportLogin.dto.TokenRefreshResponse;
import com.example.SportLogin.exception.TokenRefreshException;
import com.example.SportLogin.dto.RefreshToken;
import com.example.SportLogin.model.User;
import com.example.SportLogin.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;

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

    public ResponseEntity<?> refreshAccessToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = findByToken(requestRefreshToken);

        if (refreshToken != null) {
            verifyExpiration(refreshToken);
            User user = refreshToken.getUser();

            String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        } else {
            throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
        }
    }
}