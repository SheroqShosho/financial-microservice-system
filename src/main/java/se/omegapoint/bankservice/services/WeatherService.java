package se.omegapoint.bankservice.services;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.WeatherClient;

@Singleton
public class WeatherService {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherClient weatherClient;
    private final String apiKey;
    public WeatherService(WeatherClient weatherClient,  @Value("${OPENWEATHERMAP_API_KEY}") String value) {
        this.weatherClient = weatherClient;
        this.apiKey = value;
    }

    public Mono<Double> getWeather(String city) {
        LOG.info("Getting weather for city {}", city);

        return weatherClient.getWeatherByCity(city, apiKey, "metric")
                .map(response -> response.main().temp())
                .doOnSuccess(weatherResponse -> {LOG.info("Got weather for city sucessfully");})
                .onErrorResume(error -> {LOG.error("Failed to get weather for city", error);
                    return Mono.just(0.0);
                });

    }

    public double calculateInterestAdjustment(double temperatureCelsius) {
        LOG.info("Calculating interest adjustment based on temperature: {}°C", temperatureCelsius);

        if (temperatureCelsius < 0)  return +2.0;  // Kallt → högre ränta
        if (temperatureCelsius < 10) return +1.0;
        if (temperatureCelsius < 20) return  0.0;  // Neutralt
        if (temperatureCelsius < 30) return -1.0;
        return -2.0;                               // Varmt → lägre ränta
    }
}
