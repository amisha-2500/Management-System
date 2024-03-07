package com.example.SportLogin.controller;
import com.example.SportLogin.dto.JwtAuthResponse;
import com.example.SportLogin.dto.LoginDto;
import com.example.SportLogin.exception.TokenRefreshException;
import com.example.SportLogin.model.*;
import com.example.SportLogin.dto.*;
import com.example.SportLogin.repository.*;
import com.example.SportLogin.security.JwtTokenProvider;
import com.example.SportLogin.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private RefreshTokenService refreshTokenService;


    // Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {

        String token = authService.login(loginDto);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setRefreshToken(refreshToken.getToken());
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpDto signUpDto) {
        return authService.signUp(signUpDto);
       }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        return refreshTokenService.refreshAccessToken(request);
    }
}
