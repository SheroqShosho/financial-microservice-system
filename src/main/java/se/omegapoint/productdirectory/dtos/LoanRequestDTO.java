package se.omegapoint.productdirectory.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import se.omegapoint.productdirectory.models.enums.LoanType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;

import java.math.BigDecimal;

public record LoanRequestDTO(

//        @NotNull(message = "Is required or has an invalid format")
//        ProductType productType,

        @NotNull(message = "Is required or has an invalid format")
        ProductStatus productStatus,

        @NotNull(message = "Is required or has an invalid format")
        LoanType loanType,

        @NotNull(message = "Can not be null")
        @PositiveOrZero(message = "Can not be negative")
        BigDecimal minAmount,

        @NotNull(message = "Can not be null")
        @PositiveOrZero(message = "Can not be negative")
        BigDecimal maxAmount,

        @NotNull(message = "Can not be null")
        @PositiveOrZero(message = "Can not be negative")
        Integer durationMonths,

        @NotNull(message = "Can not be null")
        @PositiveOrZero(message = "Can not be negative")
        BigDecimal interestRate
)
{

}
