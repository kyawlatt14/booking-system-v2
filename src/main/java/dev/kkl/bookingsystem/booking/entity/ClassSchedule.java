package dev.kkl.bookingsystem.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "class_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;
    private String country;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int capacity;  // max number of users
    private int bookedCount;  // current number of users booked

    @OneToMany(mappedBy = "classSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "classSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Waitlist> waitlists;

    // Getters and Setters
}

