package se.omegapoint.bankservice.clients;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.WeatherResponseDTO;

@Client("${OPENWEATHER_API_URL}")
public interface WeatherClient {

    @Get("/weather")
    Mono<WeatherResponseDTO> getWeatherByCity(
            @QueryValue("q") String city,
            @QueryValue("appid") String apiKey,
            @QueryValue("units") String units
    );
}
