package se.omegapoint.bankservice.clients;

import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.*;


@Client("${pd1.url}")
public interface Pd1Client {

        //LOAN
        @Get("/api/loan/type/{loanType}")
        Mono<LoanTemplateRequestDTO> getLoanTemplate(@PathVariable String loanType);

        @Get("/api/loan")
        Flux<LoanTemplateResponseDTO> getAllLoanTemplates();

        @Post("/api/loan")
        Mono<LoanTemplateResponseDTO> createLoanTemplate(@Body LoanTemplateRequestDTO loanTemplateRequestDTO);

        @Put("/api/loan/{id}")
        Mono<LoanTemplateResponseDTO> updateLoanTemplate(@PathVariable String id, @Body LoanTemplateUpdateDTO loanTemplateUpdateDTO);

        @Delete("/api/loan/{id}")
        Mono<Void> deleteLoanTemplate(@PathVariable String id);


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
