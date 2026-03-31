package com.example.userservice.dtos;

import java.util.UUID;

public record LoginRequestDTO(
        String login,
        String password,
        UUID clientId
) {}
