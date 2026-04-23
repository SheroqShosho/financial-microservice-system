package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record CreditCardTemplateResponseDTO(
        String productId,
        String productType,
        String creditCardType,
        BigDecimal creditLimit,
        BigDecimal fee,
        BigDecimal interestRate,
        String productStatus,
        String description
) {
}
