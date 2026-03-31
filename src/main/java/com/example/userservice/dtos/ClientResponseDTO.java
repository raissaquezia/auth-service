package com.example.userservice.dtos;

import java.util.UUID;

public record ClientResponseDTO(UUID id, String name, Long tokenExpirationMillis) {}
