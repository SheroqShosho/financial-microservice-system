package se.omegapoint.bankservice.services;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.ExchangeRateClient;
import se.omegapoint.bankservice.dtos.ExchangeRateResponseDTO;

@Singleton
public class ExchangeRateService {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateService.class);
    private final ExchangeRateClient client;

    public ExchangeRateService(ExchangeRateClient client) {
        this.client = client;
    }

    public Mono<ExchangeRateResponseDTO> getRates (String from, String to) {
        LOG.info("Fetching exchange rate from={} to={}", from, to);

        return client.getLatesRates(from, to)
                .doOnSuccess(rate -> LOG.debug("Exchange rate fetched: {}", rate))
                .doOnError(e -> LOG.error("Error fetching exchange rate from={} to={}", from, to, e));
    }


}
