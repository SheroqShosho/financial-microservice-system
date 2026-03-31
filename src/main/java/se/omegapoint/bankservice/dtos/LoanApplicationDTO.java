package se.omegapoint.bankservice.dtos;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record LoanApplicationDTO(
        @Nullable ProfileRequestDTO profile,
        LoanRequestDTO loan
) {
}
