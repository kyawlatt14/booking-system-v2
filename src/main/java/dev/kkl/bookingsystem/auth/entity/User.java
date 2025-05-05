package dev.kkl.bookingsystem.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String fullName;

    private String country;

    private boolean emailVerified;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(Long userId) {
    }

    public enum Role {
        USER, ADMIN
    }
}

