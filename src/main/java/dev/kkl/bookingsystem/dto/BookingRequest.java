package dev.kkl.bookingsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private String className;
    private LocalDateTime classDate;
}