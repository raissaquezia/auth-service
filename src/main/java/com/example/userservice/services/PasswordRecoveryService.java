package com.example.userservice.services;

import com.example.userservice.dtos.Dtos;
import com.example.userservice.entities.PasswordResetToken;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.InvalidCredentialsException;
import com.example.userservice.repositories.PasswordResetTokenRepository;
import com.example.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String forgotPassword(Dtos.ForgotPasswordRequest request) {
        User user = userRepository.findByLoginAndClientId(request.getLogin(), request.getClientId())
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado"));

        // Invalida tokens anteriores
        tokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .userId(user.getId())
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        // Retorna o token gerado para o Controller
        return token;
    }

    @Transactional
    public void resetPassword(Dtos.ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidCredentialsException("Token de recuperação inválido"));

        if (resetToken.isUsed() || resetToken.isExpired()) {
            throw new InvalidCredentialsException("Token de recuperação expirado ou já utilizado");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado"));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }

    @Transactional
    public void changePassword(UUID userId, Dtos.ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Senha atual incorreta");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
