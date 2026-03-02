package se.omegapoint.bankservice.models;

import java.math.BigDecimal;

public class Loan {

    //Från Backend
    String loanId;
    String loanStatus;

    //Från PD1
    String loanType;
    BigDecimal interestRate;

    //Från User(frontend)
    Integer durationMonths;
    BigDecimal amount;

    public Loan(){}

    public Loan(String loanId, String loanStatus, String loanType, BigDecimal interestRate, Integer durationMonths, BigDecimal amount) {
        this.loanId = loanId;
        this.loanStatus = loanStatus;
        this.loanType = loanType;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.amount = amount;
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
