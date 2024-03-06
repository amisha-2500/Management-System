package com.example.SportLogin.repository;
import java.util.Optional;

import com.example.SportLogin.model.RefreshToken;
import com.example.SportLogin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);

}
