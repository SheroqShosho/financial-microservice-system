package se.omegapoint.authservice.dtos;

import se.omegapoint.authservice.models.Role;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long userId,
        String email,
        String firstName,
        String lastName,
        Role role,
        LocalDateTime createdAt
) {
}
