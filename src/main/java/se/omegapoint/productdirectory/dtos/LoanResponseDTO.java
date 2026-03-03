package se.omegapoint.productdirectory.dtos;

import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

public record LoanResponseDTO(
        Integer productId,
        ProductType productType,
        ProductStatus productStatus,
        String loanType,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal interestRate) {
}
