package com.soccergame.controller;

import com.soccergame.dto.auth.AuthResponse;
import com.soccergame.dto.auth.LoginRequest;
import com.soccergame.dto.auth.RegisterRequest;
import com.soccergame.dto.SimpleResponse;
import com.soccergame.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de autenticação:
 * - POST /auth/register
 * - POST /auth/login
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register attempt for username={}", request.getUsername());
        authService.register(request);
        return ResponseEntity.ok(new SimpleResponse("User registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for username={}", request.getUsername());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
