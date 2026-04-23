package se.omegapoint.productdirectory.models.loans;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import se.omegapoint.productdirectory.models.Product;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

@Entity
public class Loan extends Product {

    @Column
    @NotNull
    private String loanType;

    @Column
    @NotNull
    private BigDecimal minAmount;

    @Column
    @NotNull
    private BigDecimal maxAmount;

    @Column
    @NotNull
    private BigDecimal interestRate;

    public Loan() {
    }

    protected Loan(
            String loanType,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            BigDecimal interestRate,
            ProductType productType,
            ProductStatus productStatus,
            String description
    ) {

        super(productType, productStatus);
        this.loanType = loanType;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.interestRate = interestRate;
        this.setDescription(description);
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }


}
