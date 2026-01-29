package se.omegapoint.productdirectory.dtos;

import jakarta.validation.constraints.NotNull;
import se.omegapoint.productdirectory.models.enums.CreditCardType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

public record CreditCardRequestDTO(
        @NotNull(message = "Can not be null")
        ProductType productType,
        @NotNull(message = "Can not be null")
        ProductStatus productStatus,
        CreditCardType creditCardType,
        @NotNull(message = "Can not be null")
        BigDecimal spentAmount,
        BigDecimal creditLimit,
        @NotNull(message = "Can not be null")
        BigDecimal fee,
        BigDecimal interestRate) {


}
