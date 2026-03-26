package se.omegapoint.bankservice.controllers;

import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.*;
import se.omegapoint.bankservice.services.TemplateService;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/template")
@Secured(IS_AUTHENTICATED)
public class TemplateController {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateController.class);

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    // CREDITCARD

    @Get("/creditcards")
    public Flux<CreditCardTemplateResponseDTO> getCreditCardTemplates() {
        LOG.info("HTTP GET /template/creditcards");

        return templateService.getAll()
                .doOnNext(res -> LOG.debug("Response credit card template: {}", res))
                .doOnError(e -> LOG.error("Error in GET /template/creditcards", e));
    }

    @Post("/creditcards")
    public Mono<CreditCardTemplateResponseDTO> createCreditCardTemplate(@Body CreditCardTemplateRequestDTO request) {
        LOG.info("HTTP POST /template/creditcards");

        return templateService.createCreditCardTemplate(request)
                .doOnSuccess(res -> LOG.debug("Created credit card template response: {}", res))
                .doOnError(e -> LOG.error("Error in POST /template/creditcards", e));
    }

    @Put("/creditcards/{id}")
    public Mono<CreditCardTemplateResponseDTO> updateCreditCardTemplate(
            @PathVariable String id,
            @Body CreditCardTemplateUpdateDTO request) {

        LOG.info("HTTP PUT /template/creditcards/{}", id);

        return templateService.updateCreditCardTemplate(id, request)
                .doOnSuccess(res -> LOG.debug("Updated credit card template id={}, response={}", id, res))
                .doOnError(e -> LOG.error("Error in PUT /template/creditcards/{}", id, e));
    }

    @Delete("/creditcards/{id}")
    public Mono<Void> deleteCreditCardTemplate(@PathVariable String id) {
        LOG.info("HTTP DELETE /template/creditcards/{}", id);

        return templateService.deleteCreditCardTemplate(id)
                .doOnSuccess(unused -> LOG.debug("Deleted credit card template id={}", id))
                .doOnError(e -> LOG.error("Error in DELETE /template/creditcards/{}", id, e));
    }

    // LOAN

    @Get("/loans")
    public Flux<LoanTemplateResponseDTO> getLoanTemplates() {
        LOG.info("HTTP GET /template/loans");

        return templateService.getAllLoanTemplates()
                .doOnNext(res -> LOG.debug("Response loan template: {}", res))
                .doOnError(e -> LOG.error("Error in GET /template/loans", e));
    }

    @Post("/loans")
    public Mono<LoanTemplateResponseDTO> createLoanTemplate(@Body LoanTemplateRequestDTO request) {
        LOG.info("HTTP POST /template/loans");

        return templateService.createLoanTemplate(request)
                .doOnSuccess(res -> LOG.debug("Created loan template response: {}", res))
                .doOnError(e -> LOG.error("Error in POST /template/loans", e));
    }

    @Put("/loans/{id}")
    public Mono<LoanTemplateResponseDTO> updateLoanTemplate(
            @PathVariable String id,
            @Body LoanTemplateUpdateDTO request) {

        LOG.info("HTTP PUT /template/loans/{}", id);

        return templateService.updateLoanTemplate(id, request)
                .doOnSuccess(res -> LOG.debug("Updated loan template id={}, response={}", id, res))
                .doOnError(e -> LOG.error("Error in PUT /template/loans/{}", id, e));
    }

    @Delete("/loans/{id}")
    public Mono<Void> deleteLoanTemplate(@PathVariable String id) {
        LOG.info("HTTP DELETE /template/loans/{}", id);

        return templateService.deleteLoanTemplate(id)
                .doOnSuccess(unused -> LOG.debug("Deleted loan template id={}", id))
                .doOnError(e -> LOG.error("Error in DELETE /template/loans/{}", id, e));
    }
}


