package se.omegapoint.bankservice.clients;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.CreditCardTemplateDTO;
import se.omegapoint.bankservice.dtos.LoanTemplateDTO;

@Client("http://localhost:8080")
public interface Pd1Client {

        @Get("/api/loans/{loanType}")
        Mono<LoanTemplateDTO> getLoanTemplate(@PathVariable String loanType);

        @Get("/api/creditcards/{creditCardType}")
        Mono<CreditCardTemplateDTO> getCreditCardTemplate(@PathVariable String creditCardType);

}
