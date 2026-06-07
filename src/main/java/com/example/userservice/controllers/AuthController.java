package com.example.userservice.controllers;

import com.example.userservice.dtos.Dtos;
import com.example.userservice.entities.User;
import com.example.userservice.services.AuthService;
import com.example.userservice.services.PasswordRecoveryService;
import com.example.userservice.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SessionService sessionService;
    private final PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/login")
    public ResponseEntity<Dtos.AuthResponse> login(@RequestBody Dtos.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Dtos.AuthResponse> refresh(@RequestBody Dtos.RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Dtos.RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll() {
        User user = authService.getAuthenticatedUser();
        authService.logoutAll(user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Dtos.UserResponse> me() {
        User user = authService.getAuthenticatedUser();
        return ResponseEntity.ok(Dtos.UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole())
                .clientId(user.getClientId())
                .build());
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<Dtos.SessionResponse>> getSessions() {
        User user = authService.getAuthenticatedUser();
        List<Dtos.SessionResponse> sessions = sessionService.getUserSessions(user.getId()).stream()
                .map(s -> Dtos.SessionResponse.builder()
                        .id(s.getId())
                        .userId(s.getUserId())
                        .device(s.getDevice())
                        .ip(s.getIp())
                        .createdAt(s.getCreatedAt())
                        .lastAccess(s.getLastAccess())
                        .expiresAt(s.getExpiresAt())
                        .active(s.isActive())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessions);
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> invalidateSession(@PathVariable UUID id) {
        sessionService.invalidateSession(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Dtos.ForgotPasswordRequest request) {
        String token = passwordRecoveryService.forgotPassword(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody Dtos.ResetPasswordRequest request) {
        passwordRecoveryService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody Dtos.ChangePasswordRequest request) {
        User user = authService.getAuthenticatedUser();
        passwordRecoveryService.changePassword(user.getId(), request);
        return ResponseEntity.ok().build();
    }
}
