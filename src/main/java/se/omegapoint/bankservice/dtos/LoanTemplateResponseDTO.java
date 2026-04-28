package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record LoanTemplateResponseDTO(
        Integer productId,
        String productType,
        String productStatus,
        String loanType,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal interestRate,
        String description)
 {
}
