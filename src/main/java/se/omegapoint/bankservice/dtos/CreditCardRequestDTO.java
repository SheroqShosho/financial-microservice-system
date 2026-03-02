package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreditCardRequestDTO(
        String creditCardType
) {
}
