package se.omegapoint.productdirectory.models.creditcards;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import se.omegapoint.productdirectory.models.Product;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

@Entity
public class CreditCard extends Product {

    @Column
    @NotNull
    private String creditCardType;

    @Column
    @NotNull
    private BigDecimal creditLimit;

    @Column
    @NotNull
    private BigDecimal fee;

    @Column
    @NotNull
    private BigDecimal interestRate;

    public CreditCard() {
    }

    protected CreditCard(String creditCardType, BigDecimal creditLimit, BigDecimal fee, BigDecimal interestRate, ProductType productType, ProductStatus productStatus, String description) {
        super(productType, productStatus);
        this.creditCardType = creditCardType;
        this.creditLimit = creditLimit;
        this.fee = fee;
        this.interestRate = interestRate;
        this.setDescription(description);
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
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