package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record LoanUpdateDTO (
        String loanStatus,
        BigDecimal interestRate,
        Integer durationMonths,
        BigDecimal amount

) {


}
