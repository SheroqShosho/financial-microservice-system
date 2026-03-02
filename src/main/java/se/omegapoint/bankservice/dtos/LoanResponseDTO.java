package se.omegapoint.bankservice.dtos;

import java.math.BigDecimal;

public record LoanResponseDTO(
        String loanId,
        String loanStatus,
        String loanType,
        BigDecimal interestRate,
        Integer durationMonths,
        BigDecimal amount
) {
}
