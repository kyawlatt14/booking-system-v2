package dev.kkl.bookingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String email;
    @NotBlank private String country;
    @NotBlank private String password;
    @NotBlank private String fullName;
}
