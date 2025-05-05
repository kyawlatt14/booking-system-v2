package dev.kkl.bookingsystem.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Email(message = "Your email is not email-format.")
    @NotNull(message = "Your email must not be null.")
    private String email;
    @NotNull(message = "Password must not be null.")
    private String password;
}
