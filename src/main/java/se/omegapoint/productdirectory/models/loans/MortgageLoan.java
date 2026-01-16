package se.omegapoint.productdirectory.models.loans;

import jakarta.persistence.Entity;
import se.omegapoint.productdirectory.models.enums.LoanType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

@Entity
public class MortgageLoan extends Loan {

    protected MortgageLoan() {
    }

    protected MortgageLoan(
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
