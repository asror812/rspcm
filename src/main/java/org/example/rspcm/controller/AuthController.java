package org.example.rspcm.controller;

import org.example.rspcm.dto.auth.AuthResponse;
import org.example.rspcm.dto.auth.LoginRequest;
import org.example.rspcm.dto.auth.RegisterRequest;
import org.example.rspcm.dto.auth.ResendOtpRequest;
import org.example.rspcm.dto.auth.VerifyOtpRequest;
import org.example.rspcm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(Map.of("message", authService.verifyOtp(request)));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, String>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        return ResponseEntity.ok(Map.of("message", authService.resendOtp(request.email())));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/swagger-admin-token")
    public ResponseEntity<AuthResponse> swaggerAdminToken() {
        return ResponseEntity.ok(authService.issueSwaggerAdminToken());
    }

    @PostMapping("/swagger-panel-token")
    public ResponseEntity<Map<String, String>> swaggerPanelToken() {
        return ResponseEntity.ok(Map.of("token", authService.resolveSwaggerPanelToken()));
    }
}
