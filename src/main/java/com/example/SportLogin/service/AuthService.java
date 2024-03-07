package com.example.SportLogin.service;

import com.example.SportLogin.dto.LoginDto;
import com.example.SportLogin.dto.SignUpDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AuthService {
    String login(LoginDto loginDto);
    ResponseEntity<String> signUp(SignUpDto signUpDto);
}
