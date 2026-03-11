package se.omegapoint.bankservice.services;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.Pd1Client;
import se.omegapoint.bankservice.models.CreditCard;
import se.omegapoint.bankservice.repositories.CustomerRegisterRepository;

import java.util.UUID;


@Singleton
public class CreditCardService {

    private final CustomerRegisterRepository repository;
    private final Pd1Client pd1Client;

    public CreditCardService(CustomerRegisterRepository repository, Pd1Client pd1Client) {
        this.repository = repository;
        this.pd1Client = pd1Client;
    }

    public Mono<CreditCard> createCreditCard(String userId, String creditCardType) {

        return pd1Client.getCreditCardTemplate(creditCardType)
                .flatMap(template -> {
                    CreditCard creditCard = new CreditCard(
                            userId,
                            UUID.randomUUID().toString(),
                            template.creditCardType(),
                            template.creditLimit(),
                            template.fee(),
                            template.interestRate()
                    );
                    return repository.saveCreditCard(creditCard);
                });
    }

    public Flux<CreditCard> getAllCreditCardsFromUser(String userId) {

        return repository.getAllCreditCardsFromUser(userId);
    }

    public Mono<Void> deleteCreditCardById(String userId, String creditCardType, String creditCardId) {

        return repository.deleteCreditCardById(userId, creditCardType, creditCardId);
    }
}
