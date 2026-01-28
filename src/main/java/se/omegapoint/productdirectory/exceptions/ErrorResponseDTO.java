package se.omegapoint.productdirectory.exceptions;

import java.time.LocalDateTime;

// DTO för att visa begränsad information i error-message
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
