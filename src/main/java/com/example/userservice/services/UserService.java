package com.example.userservice.services;

import com.example.userservice.dtos.Dtos;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.UserAlreadyExistsException;
import com.example.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(Dtos.UserRegistrationRequest request) {
        if (userRepository.findByLoginAndClientId(request.getLogin(), request.getClientId()).isPresent()) {
            throw new UserAlreadyExistsException("Usuário já existe para este cliente");
        }

        User user = User.builder()
                .login(request.getLogin())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .clientId(request.getClientId())
                .role(request.getRole())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public Dtos.UserResponse updateUser(UUID userId, Dtos.UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (request.getLogin() != null) user.setLogin(request.getLogin());
        if (request.getRole() != null) user.setRole(request.getRole());

        userRepository.save(user);

        return Dtos.UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole())
                .clientId(user.getClientId())
                .build();
    }

    @Transactional
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    public Dtos.UserResponse findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return Dtos.UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole())
                .clientId(user.getClientId())
                .build();
    }
}
