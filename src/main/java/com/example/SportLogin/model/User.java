package com.example.SportLogin.model;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Getter
@Setter
@Table(name = "users")
public class User{

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}