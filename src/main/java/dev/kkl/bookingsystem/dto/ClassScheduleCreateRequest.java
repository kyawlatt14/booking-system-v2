package dev.kkl.bookingsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassScheduleCreateRequest {
    private String className;
    private String country;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int capacity;

    // Getters and Setters
}

