package dev.kkl.bookingsystem.booking.entity;

import dev.kkl.bookingsystem.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_schedule_id")
    private ClassSchedule classSchedule;

    private String status;  // "BOOKED", "CANCELED", etc.

    private String className;
    private LocalDateTime classDate;

    private LocalDateTime bookedTime;

    private LocalDateTime bookedAt;
}

