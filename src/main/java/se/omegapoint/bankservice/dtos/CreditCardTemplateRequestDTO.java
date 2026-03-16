package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record  CreditCardTemplateRequestDTO (
        String productStatus,
        String creditCardType,
        BigDecimal creditLimit,
        BigDecimal fee,
        BigDecimal interestRate
) {
}
