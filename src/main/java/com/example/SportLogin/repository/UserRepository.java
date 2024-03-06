package com.example.SportLogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SportLogin.model.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
   User findByEmail(String email);
    User findByUsernameOrEmail(String username, String email);
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);


}
