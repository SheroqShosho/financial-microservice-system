package se.omegapoint.authservice.dtos;

public record GoogleUserInfoDTO(
        String googleId,
        String email,
        String firstName,
        String lastName
) {
}
