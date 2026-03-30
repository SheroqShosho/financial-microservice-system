package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.Map;

@Serdeable
public record ExchangeRateResponseDTO(
        String base,
        String date,
        Map<String, BigDecimal> rates
) {
}
