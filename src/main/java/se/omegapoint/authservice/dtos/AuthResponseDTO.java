package se.omegapoint.authservice.dtos;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
    // Sätter tokenType till "Bearer" automatiskt
    public AuthResponseDTO(String accessToken, String refreshToken, long expiresIn) {
        this(accessToken, refreshToken, "Bearer", expiresIn);
    }
}
