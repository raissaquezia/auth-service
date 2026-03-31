package com.example.userservice.controllers;

import com.example.userservice.dtos.LoginRequestDTO;
import com.example.userservice.dtos.RegisterRequestDTO;
import com.example.userservice.dtos.TokenResponseDTO;
import com.example.userservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO request) {
        TokenResponseDTO response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequestDTO request) {
        authService.register(request);

        return ResponseEntity.status(201).build();
    }
}