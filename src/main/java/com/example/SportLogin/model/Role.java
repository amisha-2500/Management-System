package com.example.SportLogin.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

}
