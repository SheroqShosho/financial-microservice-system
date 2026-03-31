package se.omegapoint.bankservice.dtos;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record WeatherResponseDTO(
        MainData main,
        String name
) {
    @Serdeable
    public record MainData(double temp) {}
}
