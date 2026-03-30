package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public record MyPagesResponseDTO(
        ProfileResponseDTO profile,
        List<LoanResponseDTO> loans,
        List<CreditCardResponseDTO> creditCards


) {
}
