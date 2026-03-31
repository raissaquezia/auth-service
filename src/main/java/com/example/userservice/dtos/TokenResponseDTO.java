package com.example.userservice.dtos;

public record TokenResponseDTO(
        String token,
        Long expiresIn
) {}
