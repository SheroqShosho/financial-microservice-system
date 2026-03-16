package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record LoanTemplateRequestDTO(
        String productStatus,
        String loanType,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal interestRate
        //String productStatus, ska vi ha med denna för validering av mallen ifall den är aktiv eller inactive?
){
}
