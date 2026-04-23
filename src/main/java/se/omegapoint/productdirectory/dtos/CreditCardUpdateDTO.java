package se.omegapoint.productdirectory.dtos;

import jakarta.validation.constraints.PositiveOrZero;
import se.omegapoint.productdirectory.models.enums.ProductStatus;

import java.math.BigDecimal;

public record CreditCardUpdateDTO(
        ProductStatus productStatus,

        String creditCardType,

        @PositiveOrZero(message = "Can not be negative")
        BigDecimal creditLimit,

        @PositiveOrZero(message = "Can not be negative")
        BigDecimal fee,

        @PositiveOrZero(message = "Can not be negative")
        BigDecimal interestRate,

        String description
) {
}