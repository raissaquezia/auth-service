package com.example.userservice.dtos;

import java.util.UUID;

public record RegisterRequestDTO(
        String login,
        String password,
        String role,
        UUID clientId
) {}
