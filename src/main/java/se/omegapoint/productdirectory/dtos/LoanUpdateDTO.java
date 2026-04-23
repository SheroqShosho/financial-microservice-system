package se.omegapoint.productdirectory.dtos;

import jakarta.validation.constraints.PositiveOrZero;
import se.omegapoint.productdirectory.models.enums.ProductStatus;

import java.math.BigDecimal;

public record LoanUpdateDTO(
        ProductStatus productStatus,

        String loanType,

        @PositiveOrZero(message = "Can not be negative")
        BigDecimal minAmount,

        @PositiveOrZero(message = "Can not be negative")
        BigDecimal maxAmount,

        @PositiveOrZero(message = "Can not be negative")
        BigDecimal interestRate,

        String description
) {
}