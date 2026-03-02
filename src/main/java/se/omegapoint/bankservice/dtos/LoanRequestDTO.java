package se.omegapoint.bankservice.dtos;

import java.math.BigDecimal;

public record LoanRequestDTO(
        String loanType,
        Integer durationMonths,
        BigDecimal amount
) {
}
