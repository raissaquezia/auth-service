package com.example.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Dtos {

    // User DTOs

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRegistrationRequest {
        private String login;
        private String password;
        private String clientId;
        private String role;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateRequest {
        private String login;
        private String role;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private UUID id;
        private String login;
        private String role;
        private String clientId;
    }

    // Client DTOs

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientRegistrationRequest {
        private String clientName;
        private List<String> redirectUris;
        private List<String> scopes;
        private List<String> grantTypes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientRegistrationResponse {
        private String clientId;
        private String clientSecret;
    }


    // Auth DTOs

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String login;
        private String password;
        private String clientId;
        private String role;
        private String device;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;
        private Long expiresIn;
        private Long refreshExpiresIn;
        private String tokenType;
        private UserResponse user;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenRequest {
        private String refreshToken;
    }

    // Session DTOs

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionResponse {
        private UUID id;
        private UUID userId;
        private String device;
        private String ip;
        private LocalDateTime createdAt;
        private LocalDateTime lastAccess;
        private LocalDateTime expiresAt;
        private boolean active;
    }

    // Password DTOs

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPasswordRequest {
        private String login;
        private String clientId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPasswordRequest {
        private String token;
        private String newPassword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }

}
