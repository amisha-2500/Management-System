package com.example.SportLogin.service;

import com.example.SportLogin.dto.LoginDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AuthService {
    String login(LoginDto loginDto);

}
