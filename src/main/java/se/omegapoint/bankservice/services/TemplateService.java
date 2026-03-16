package se.omegapoint.bankservice.services;


import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.Pd1Client;
import se.omegapoint.bankservice.dtos.CreditCardTemplateRequestDTO;
import se.omegapoint.bankservice.dtos.CreditCardTemplateResponseDTO;
import se.omegapoint.bankservice.dtos.CreditCardTemplateUpdateDTO;

@Singleton
public class TemplateService {

    private final Pd1Client pd1Client;


    public TemplateService(Pd1Client pd1Client) {
        this.pd1Client = pd1Client;
    }

    public Flux<CreditCardTemplateResponseDTO> getAll() {
        return pd1Client.getAllCreditCardTemplates();
}

    public Mono<CreditCardTemplateResponseDTO> createCreditCardTemplate(CreditCardTemplateRequestDTO creditCardTemplateRequestDTO) {
        return pd1Client.createCreditCardTemplate(creditCardTemplateRequestDTO);
    }

    public Mono<CreditCardTemplateResponseDTO> updateCreditCardTemplate(String id, CreditCardTemplateUpdateDTO creditCardTemplateUpdateDTO) {
        return pd1Client.updateCreditCardTemplate(id, creditCardTemplateUpdateDTO);
    }

    public Mono<Void> deleteCreditCardTemplate(String id) {
        return pd1Client.deleteCreditCardTemplate(id);
    }
}
