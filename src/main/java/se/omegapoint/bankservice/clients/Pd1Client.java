package se.omegapoint.bankservice.clients;

import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.CreditCardTemplateRequestDTO;
import se.omegapoint.bankservice.dtos.CreditCardTemplateResponseDTO;
import se.omegapoint.bankservice.dtos.CreditCardTemplateUpdateDTO;
import se.omegapoint.bankservice.dtos.LoanTemplateDTO;


@Client("http://localhost:8081")
public interface Pd1Client {

        //LOAN
        @Get("/api/loan/type/{loanType}")
        Mono<LoanTemplateDTO> getLoanTemplate(@PathVariable String loanType);

        //CREDITCARD
        @Get("/api/creditcard/type/{creditCardType}")
        Mono<CreditCardTemplateResponseDTO> getCreditCardTemplate(@PathVariable String creditCardType);

        @Get("/api/creditcard")
        Flux<CreditCardTemplateResponseDTO> getAllCreditCardTemplates();

        @Post("/api/creditcard")
        Mono<CreditCardTemplateResponseDTO> createCreditCardTemplate(@Body CreditCardTemplateRequestDTO creditCardTemplateRequest);

        @Put("/api/creditcard/{id}")
        Mono<CreditCardTemplateResponseDTO> updateCreditCardTemplate(@PathVariable String id, @Body CreditCardTemplateUpdateDTO creditCardTemplateUpdateDTO);

        @Delete("/api/creditcard/{id}")
        Mono<Void> deleteCreditCardTemplate(@PathVariable String id);
}
