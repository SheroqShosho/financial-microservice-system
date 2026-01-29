package se.omegapoint.productdirectory.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import se.omegapoint.productdirectory.models.enums.CreditCardType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;

import java.math.BigDecimal;

public record CreditCardRequestDTO(
//        @NotNull(message = "Is required or has an invalid format")
//        ProductType productType,

        @NotNull(message = "Is required or has an invalid format")
        ProductStatus productStatus,

        @NotNull(message = "Is required or has an invalid format")
        CreditCardType creditCardType,

        @NotNull(message = "Can not be null")
        @PositiveOrZero(message = "Can not be negative")
        BigDecimal spentAmount,

        @NotNull(message = "Can not be null")
        @PositiveOrZero(message = "Can not be negative")
        BigDecimal creditLimit,

        @NotNull(message = "Can not be null")
        @PositiveOrZero(message = "Can not be negative")
        BigDecimal fee,

        @NotNull(message = "Can not be null")
        @PositiveOrZero(message = "Can not be negative")
        BigDecimal interestRate
)
{


}
