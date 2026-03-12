package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record ProfileUpdateDTO (
         String country,
         String city,
         String address,
         String zipCode,
         String phoneNumber,
         BigDecimal yearlyIncome,
         String status
) {
}
