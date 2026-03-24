package com.gioele.musicbox.service;

import com.gioele.musicbox.dto.AuthResponse;
import com.gioele.musicbox.dto.LoginRequest;
import com.gioele.musicbox.dto.RegisterRequest;
import com.gioele.musicbox.entity.User;
import com.gioele.musicbox.repository.UserRepository;
import com.gioele.musicbox.security.JwtService;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(BAD_REQUEST, "Email già registrata");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Credenziali non valide"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Credenziali non valide");
        }

        return buildAuthResponse(user);
    }

    public AuthResponse me(User user) {
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user.getEmail());
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @PostConstruct
    public void seedAdmin() {
        String email = "admin@musicbox.local";
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(User.builder()
                    .name("Admin Demo")
                    .email(email)
                    .password(passwordEncoder.encode("Admin123!"))
                    .role("ADMIN")
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }
}
