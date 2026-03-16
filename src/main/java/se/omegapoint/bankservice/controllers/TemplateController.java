package se.omegapoint.bankservice.controllers;

import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.CreditCardTemplateRequestDTO;
import se.omegapoint.bankservice.dtos.CreditCardTemplateResponseDTO;
import se.omegapoint.bankservice.dtos.CreditCardTemplateUpdateDTO;
import se.omegapoint.bankservice.services.TemplateService;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller("/template")
@Secured(IS_ANONYMOUS)
public class TemplateController {

    private final TemplateService templateService;


    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Get("/creditcards")
    public Flux<CreditCardTemplateResponseDTO> getCreditCardTemplates() {
        return templateService.getAll();
    }

    @Post("/creditcards")
    public Mono<CreditCardTemplateResponseDTO> createCreditCardTemplate(@Body CreditCardTemplateRequestDTO creditCardTemplateRequestDTO) {
        return templateService.createCreditCardTemplate(creditCardTemplateRequestDTO);
    }

    @Put("/creditcards/{id}")
    public Mono<CreditCardTemplateResponseDTO> updateCreditCardTemplate(@PathVariable String id, @Body CreditCardTemplateUpdateDTO creditCardTemplateUpdateDTO) {
        return templateService.updateCreditCardTemplate(id, creditCardTemplateUpdateDTO);
    }

    @Delete("/creditcards/{id}")
    public Mono<Void> deleteCreditCardTemplate(@PathVariable String id) {
        return templateService.deleteCreditCardTemplate(id);
    }

}


