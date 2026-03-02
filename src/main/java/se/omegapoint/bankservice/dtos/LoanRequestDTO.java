package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record LoanRequestDTO(
        String loanType,
        Integer durationMonths,
        BigDecimal amount
) {
}
