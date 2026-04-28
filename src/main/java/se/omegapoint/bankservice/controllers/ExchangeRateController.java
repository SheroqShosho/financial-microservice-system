package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.ExchangeRateResponseDTO;
import se.omegapoint.bankservice.services.ExchangeRateService;


@Controller("exchange")
@Secured(SecurityRule.IS_ANONYMOUS)
public class ExchangeRateController {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateController.class);

    private final ExchangeRateService service;

    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    @Get("/{from}/{to}")

    public Mono<MutableHttpResponse<ExchangeRateResponseDTO>> getExchangeRate (
            @PathVariable String from,
            @PathVariable String to) {

        LOG.info("HTTP get /currency/{}/{}", from, to );

        return service.getRates(from, to)
                .map(HttpResponse::ok)
                .onErrorResume(e -> {
                    LOG.error("ERROR get /currency/{}/{}", from, to, e);
                    return Mono.just(HttpResponse.serverError());
                });

    }


}
