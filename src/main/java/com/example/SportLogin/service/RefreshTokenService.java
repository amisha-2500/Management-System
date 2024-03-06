package com.example.SportLogin.service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.example.SportLogin.dto.LoginDto;
import com.example.SportLogin.exception.TokenRefreshException;
import com.example.SportLogin.model.RefreshToken;
import com.example.SportLogin.model.User;
import com.example.SportLogin.repository.RefreshTokenRepository;
import com.example.SportLogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RefreshTokenService {

    private Long refreshTokenDurationMs = (long) (8*60*60*1000);

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken findByToken(String token) {

        return refreshTokenRepository.findByToken(token);
    }


    public RefreshToken createRefreshToken(LoginDto loginDto) {
        RefreshToken refreshToken = new RefreshToken();
        String username = loginDto.getUsername();

        refreshToken.setUser(userRepository.findByUsername(username));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }
}
