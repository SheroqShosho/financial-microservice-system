package se.omegapoint.productdirectory.models.creditcards;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import se.omegapoint.productdirectory.models.Product;
import se.omegapoint.productdirectory.models.enums.CreditCardType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

@Entity
public class CreditCard extends Product {

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Creditcard type required.")
    private CreditCardType creditCardType;

    @Column
    @NotNull
    private BigDecimal spentAmount;

    @Column
    @NotNull
    private BigDecimal creditLimit;

    @Column
    @NotNull(message = "TEST NULL")
    private BigDecimal fee;

    @Column
    @NotNull
    private BigDecimal interestRate;

    public CreditCard() {
    }

    protected CreditCard(CreditCardType creditCardType, BigDecimal spentAmount, BigDecimal creditLimit, BigDecimal fee, BigDecimal interestRate, ProductType productType, ProductStatus productStatus) {
        super(productType, productStatus);
        this.creditCardType = creditCardType;
        this.spentAmount = spentAmount;
        this.creditLimit = creditLimit;
        this.fee = fee;
        this.interestRate = interestRate;
    }

    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}