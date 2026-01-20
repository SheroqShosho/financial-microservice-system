package se.omegapoint.productdirectory.models.loans;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import se.omegapoint.productdirectory.models.Product;
import se.omegapoint.productdirectory.models.enums.LoanType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

@Entity
public class Loan extends Product {

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private LoanType loanType;

    @Column
    @NotNull
    private BigDecimal minAmount;

    @Column
    @NotNull
    private BigDecimal maxAmount;

    @Column
    @NotNull
    private Integer durationMonths;

    @Column
    @NotNull
    private BigDecimal interestRate;

    public Loan() {
    }

    protected Loan(
            LoanType loanType,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Integer durationMonths,
            BigDecimal interestRate,
            ProductType productType,
            ProductStatus productStatus
    ) {

        super(productType, productStatus);
        this.loanType = loanType;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.durationMonths = durationMonths;
        this.interestRate = interestRate;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
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

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }


}
