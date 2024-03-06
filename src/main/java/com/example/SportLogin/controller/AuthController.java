package com.example.SportLogin.controller;
import com.example.SportLogin.dto.JwtAuthResponse;
import com.example.SportLogin.dto.LoginDto;
import com.example.SportLogin.exception.TokenRefreshException;
import com.example.SportLogin.model.*;
import com.example.SportLogin.dto.*;
import com.example.SportLogin.model.*;
import com.example.SportLogin.repository.*;
import com.example.SportLogin.security.JwtTokenProvider;
import com.example.SportLogin.service.*;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private RefreshTokenService refreshTokenService;
    private JwtTokenProvider jwtTokenProvider;


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
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {

        // add check for username exists in a DB

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // add check for email exists in DB
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // create user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);

        if (refreshToken != null) {
            refreshTokenService.verifyExpiration(refreshToken);
            User user = refreshToken.getUser();

            String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        } else {
            throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
        }
    }
}
