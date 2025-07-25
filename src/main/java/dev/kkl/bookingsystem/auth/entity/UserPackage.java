package dev.kkl.bookingsystem.auth.entity;

import dev.kkl.bookingsystem.pack.entity.Package;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_packages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Package pkg;

    private int remainingCredits;
    private LocalDateTime purchaseDate;
    private LocalDateTime expiryDate;
}
