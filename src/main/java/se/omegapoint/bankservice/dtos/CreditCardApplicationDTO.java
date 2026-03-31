package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;

@Serdeable
public record CreditCardApplicationDTO (
        @Nullable ProfileRequestDTO profile,
        CreditCardRequestDTO creditcard
) {
}
