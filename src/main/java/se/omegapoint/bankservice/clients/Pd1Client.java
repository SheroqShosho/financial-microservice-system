package se.omegapoint.bankservice.clients;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.CreditCardTemplateDTO;
import se.omegapoint.bankservice.dtos.LoanTemplateDTO;

@Client("http://localhost:8081")
public interface Pd1Client {

        @Get("/api/loan/type/{loanType}")
        Mono<LoanTemplateDTO> getLoanTemplate(@PathVariable String loanType);

        @Get("/api/creditcard/type/{creditCardType}")
        Mono<CreditCardTemplateDTO> getCreditCardTemplate(@PathVariable String creditCardType);

}
