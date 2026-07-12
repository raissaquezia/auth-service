package com.example.userservice.services;

import com.example.userservice.dtos.Dtos;
import com.example.userservice.entities.Session;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.InvalidCredentialsException;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionService sessionService;
    private final HttpServletRequest request;

    @Transactional
    public Dtos.AuthResponse login(Dtos.LoginRequest loginRequest) {
        User user = userRepository.findByLoginAndClientId(loginRequest.getLogin(), loginRequest.getClientId())
                .orElseThrow(() -> new InvalidCredentialsException("Usuário ou Client ID inválidos"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Senha inválida");
        }

        if (loginRequest.getRole() == null || !loginRequest.getRole().equals(user.getRole())) {
            throw new InvalidCredentialsException("Role inválida");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        sessionService.createSession(
                user.getId(),
                loginRequest.getDevice(),
                request.getRemoteAddr(),
                refreshToken,
                jwtService.getRefreshTokenExpiration()
        );

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    @Transactional
    public Dtos.AuthResponse refresh(String refreshToken) {
        Session session = sessionService.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidCredentialsException("Refresh token inválido ou expirado"));

        User user = userRepository.findById(session.getUserId())
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado"));

        // Invalida a sessão antiga (estratégia de rotação de refresh token)
        session.setActive(false);
        
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        sessionService.createSession(
                user.getId(),
                session.getDevice(),
                request.getRemoteAddr(),
                newRefreshToken,
                jwtService.getRefreshTokenExpiration()
        );

        return buildAuthResponse(user, newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        sessionService.invalidateByRefreshToken(refreshToken);
    }

    @Transactional
    public void logoutAll(UUID userId) {
        sessionService.invalidateAllUserSessions(userId);
    }

    public User getAuthenticatedUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findAll().stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não autenticado"));
    }

    private Dtos.AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        return Dtos.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getAccessTokenExpiration())
                .refreshExpiresIn(jwtService.getRefreshTokenExpiration())
                .tokenType("Bearer")
                .user(Dtos.UserResponse.builder()
                        .id(user.getId())
                        .login(user.getLogin())
                        .role(user.getRole())
                        .clientId(user.getClientId())
                        .build())
                .build();
    }
}
