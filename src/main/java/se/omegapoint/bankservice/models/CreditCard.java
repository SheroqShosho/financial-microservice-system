package se.omegapoint.bankservice.models;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@DynamoDbBean
public class CreditCard {

    private String userId;
    private String creditCardId ;
    private String creditCardType;
    private String creditLimit;
    private BigDecimal fee;
    private BigDecimal interestRate;
    private BigDecimal spentAmount;
    private String status;

    public CreditCard() {
    }

    public CreditCard(String creditCardId, String creditCardType, String creditLimit, BigDecimal fee, BigDecimal interestRate, BigDecimal spentAmount) {
        this.creditCardId = creditCardId;
        this.creditCardType = creditCardType;
        this.creditLimit = creditLimit;
        this.fee = fee;
        this.interestRate = interestRate;
        this.spentAmount = spentAmount;
        this.status = "ACTIVE";
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getUserId() {
        return "USER#" + userId;
    }

    public void setUserId(String userId) {
        this.userId = userId.replace("USER#", "");
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getSk() {
        return "CARD#" + creditCardType + "#" + creditCardId;
    }

    public void setSk(String sk) {
        // SK ÄR ALLTID "CARD#"
    }

    public String getRawUserId() {
        return userId;
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
