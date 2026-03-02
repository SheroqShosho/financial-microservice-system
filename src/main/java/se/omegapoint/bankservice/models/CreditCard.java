package se.omegapoint.bankservice.models;

import java.math.BigDecimal;

public class CreditCard {

    private String creditCardId ;
    private String creditCardType;
    private String creditLimit;
    private BigDecimal fee;
    private BigDecimal interestRate;
    private BigDecimal spentAmount;
    private String status;

    public CreditCard() {
    }

    public CreditCard(String creditCardId, String creditCardType, String creditLimit, BigDecimal fee, BigDecimal interestRate, BigDecimal spentAmount, String status) {
        this.creditCardId = creditCardId;
        this.creditCardType = creditCardType;
        this.creditLimit = creditLimit;
        this.fee = fee;
        this.interestRate = interestRate;
        this.spentAmount = spentAmount;
        this.status = status;
    }

    public String getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
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

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
