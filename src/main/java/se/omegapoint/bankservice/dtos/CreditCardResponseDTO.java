package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record CreditCardResponseDTO(
        String userId,
        String creditCardId,
        String creditCardType,
        BigDecimal creditLimit,
        BigDecimal fee,
        BigDecimal interestRate,
        BigDecimal spentAmount,
        BigDecimal availableAmount,
        String status
) {
}