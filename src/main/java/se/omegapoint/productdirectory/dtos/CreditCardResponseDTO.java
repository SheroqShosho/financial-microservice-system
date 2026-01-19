package se.omegapoint.productdirectory.dtos;

import se.omegapoint.productdirectory.models.enums.CreditCardType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

public record CreditCardResponseDTO(
        Integer productId,
        ProductType productType,
        ProductStatus productStatus,
        CreditCardType creditCardType,
        BigDecimal spentAmount,
        BigDecimal creditLimit,
        BigDecimal fee,
        BigDecimal interestRate
) {
}
