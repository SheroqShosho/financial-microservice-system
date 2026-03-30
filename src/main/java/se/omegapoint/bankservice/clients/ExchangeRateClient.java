package se.omegapoint.bankservice.clients;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.ExchangeRateResponseDTO;

@Client("${EXCHANGE_RATE_URL}")
public interface ExchangeRateClient {

    @Get("/latest")
    Mono<ExchangeRateResponseDTO> getLatesRates (
            @QueryValue ("from") String fromCurrency,
            @QueryValue ("to") String toCurrency
    );


}
