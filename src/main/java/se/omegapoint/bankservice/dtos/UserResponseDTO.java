package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
public record UserResponseDTO(
        String firstName,
        String lastName,
        String email,
        List<CreditCardResponseDTO> creditCardsRes,
        BigDecimal totalCreditDebt
) {

}
