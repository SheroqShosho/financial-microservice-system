package se.omegapoint.bankservice.models;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@DynamoDbBean
public class Loan {


    String userId;
    String loanId;
    String loanStatus;
    String loanType;
    BigDecimal interestRate;
    Integer durationMonths;
    BigDecimal amount;

    public Loan(){}

    public Loan(String userId, String loanId, String loanStatus, String loanType, BigDecimal interestRate, Integer durationMonths, BigDecimal amount) {
        this.userId = userId;
        this.loanId = loanId;
        this.loanStatus = loanStatus;
        this.loanType = loanType;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.amount = amount;
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
        return "LOAN#" + loanType + "#" + loanId;
    }

    public void setSk(String sk) {
        //SK ÄR ALLTID LOAN#
    }

    public String getRawUserId() {
        return userId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }
}
