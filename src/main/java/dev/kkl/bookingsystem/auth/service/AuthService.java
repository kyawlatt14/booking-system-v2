package dev.kkl.bookingsystem.auth.service;

import dev.kkl.bookingsystem.base.common.AppResponse;
import dev.kkl.bookingsystem.base.config.JwtTokenProvider;
import dev.kkl.bookingsystem.auth.dto.LoginRequest;
import dev.kkl.bookingsystem.auth.dto.RegisterRequest;
import dev.kkl.bookingsystem.auth.entity.User;
import dev.kkl.bookingsystem.base.exception.ApplicationErrorException;
import dev.kkl.bookingsystem.auth.repository.UserRepository;
import dev.kkl.bookingsystem.base.common.service.MockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dev.kkl.bookingsystem.base.common.Constant.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;
    private final MockService mockService;

    public AppResponse register(RegisterRequest request) {

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new ApplicationErrorException(USER_EMAIL_EXISTED);
        }

        User user = new User();
        user.setCountry(request.getCountry().toLowerCase());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(User.Role.USER);
        user.setEmailVerified(true); // For demo only

        userRepo.save(user);

        // mock email verification
        mockService.verificationForEmail(request.getEmail());

        return AppResponse.success(USER_REGISTERED, HttpStatus.CREATED);
    }

    public String login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApplicationErrorException(AUTH_FAIL));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApplicationErrorException(USER_PASSWORD_NOT_MATCH);
        }

        return jwtProvider.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name()))
                ));
    }
}
