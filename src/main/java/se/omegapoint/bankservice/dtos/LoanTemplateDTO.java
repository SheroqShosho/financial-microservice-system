package se.omegapoint.bankservice.dtos;

import java.math.BigDecimal;

public record LoanTemplateDTO (
        String loanType,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal interestRate
        //String productStatus, ska vi ha med denna för validering av mallen ifall den är aktiv eller inactive?
){
}
