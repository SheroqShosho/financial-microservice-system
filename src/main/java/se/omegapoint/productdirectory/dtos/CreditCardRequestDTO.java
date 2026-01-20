package se.omegapoint.productdirectory.dtos;

import se.omegapoint.productdirectory.models.enums.CreditCardType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

public record CreditCardRequestDTO(
        ProductType productType,
        ProductStatus productStatus,
        CreditCardType creditCardType,
        BigDecimal spentAmount,
        BigDecimal creditLimit,
        BigDecimal fee,
        BigDecimal interestRate)
{


}
