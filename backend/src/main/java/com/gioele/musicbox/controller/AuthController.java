package com.gioele.musicbox.controller;

import com.gioele.musicbox.dto.AuthResponse;
import com.gioele.musicbox.dto.LoginRequest;
import com.gioele.musicbox.dto.RegisterRequest;
import com.gioele.musicbox.entity.User;
import com.gioele.musicbox.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public AuthResponse me(@AuthenticationPrincipal User user) {
        return authService.me(user);
    }
}
