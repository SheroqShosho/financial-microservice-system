package se.omegapoint.productdirectory.dtos;

import se.omegapoint.productdirectory.models.enums.LoanType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

public record LoanResponseDTO(
        Integer productId,
        ProductType productType,
        ProductStatus productStatus,
        LoanType loanType,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        Integer durationMonths,
        BigDecimal interestRate) {
}
