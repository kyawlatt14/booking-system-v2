package dev.kkl.bookingsystem.auth.controller;

import dev.kkl.bookingsystem.auth.service.AuthService;
import dev.kkl.bookingsystem.auth.dto.LoginRequest;
import dev.kkl.bookingsystem.auth.dto.RegisterRequest;
import dev.kkl.bookingsystem.base.common.AppResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth-Controller")
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AppResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

