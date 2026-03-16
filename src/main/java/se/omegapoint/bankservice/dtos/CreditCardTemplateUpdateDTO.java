package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record CreditCardTemplateUpdateDTO (
        BigDecimal creditLimit,
        BigDecimal fee,
        BigDecimal interestRate,
        String status
) {
}