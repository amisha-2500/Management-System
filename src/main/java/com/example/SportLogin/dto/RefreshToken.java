package com.example.SportLogin.dto;

import com.example.SportLogin.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;


@Getter
@Setter
public class RefreshToken {
    private User user;
    private String token;
    private Instant expiryDate;

}
