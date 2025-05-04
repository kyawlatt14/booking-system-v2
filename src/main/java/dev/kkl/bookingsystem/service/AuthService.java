package dev.kkl.bookingsystem.service;

import dev.kkl.bookingsystem.config.JwtTokenProvider;
import dev.kkl.bookingsystem.dto.LoginRequest;
import dev.kkl.bookingsystem.dto.RegisterRequest;
import dev.kkl.bookingsystem.entity.User;
import dev.kkl.bookingsystem.exception.ApplicationErrorException;
import dev.kkl.bookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;
    private final MockService mockService;

    public void register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new ApplicationErrorException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(User.Role.USER);
        user.setEmailVerified(true); // For demo only

        userRepo.save(user);

        // mock email verification
        mockService.verificationForEmail(request.getEmail());
    }

    public String login(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApplicationErrorException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApplicationErrorException("Invalid credentials");
        }

        return jwtProvider.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name()))
                ));
    }
}
