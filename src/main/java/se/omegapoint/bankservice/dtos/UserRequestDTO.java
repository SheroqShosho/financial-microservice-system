package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UserRequestDTO(
    String firstName,
    String lastName,
    String email
) {
}
