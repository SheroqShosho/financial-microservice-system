package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UserResponseDTO(
        String firstName,
        String lastName,
        String email
) {

}
