package se.omegapoint.productdirectory.models.loans;

import jakarta.persistence.*;
import se.omegapoint.productdirectory.models.enums.LoanType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

@Entity
public class PrivateLoan extends Loan {


    protected PrivateLoan() {}

    protected PrivateLoan(
                       LoanType loanType,
                       BigDecimal minAmount,
                       BigDecimal maxAmount,
                       int durationMonths,
                       BigDecimal interestRate,
                       ProductType productType,
                       ProductStatus productStatus
    ) {

        super(
                loanType,
                minAmount,
                maxAmount,
                durationMonths,
                interestRate,
                productType,
                productStatus
        );
    }


}
