package dev.kkl.bookingsystem.pack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "packages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String country;
    private int totalCredits;
    private double price;
    private int validityDays; // number of days after purchase
}

