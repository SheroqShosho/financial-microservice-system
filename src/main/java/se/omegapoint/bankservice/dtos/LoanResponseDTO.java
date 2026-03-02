package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record LoanResponseDTO(
        String loanId,
        String loanStatus,
        String loanType,
        BigDecimal interestRate,
        Integer durationMonths,
        BigDecimal amount
) {
}
