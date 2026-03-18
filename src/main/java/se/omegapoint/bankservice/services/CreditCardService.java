package se.omegapoint.bankservice.services;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.Pd1Client;
import se.omegapoint.bankservice.dtos.CreditCardResponseDTO;
import se.omegapoint.bankservice.dtos.CreditCardUpdateDTO;
import se.omegapoint.bankservice.models.CreditCard;
import se.omegapoint.bankservice.repositories.CustomerRegisterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Singleton
public class CreditCardService {

    private final CustomerRegisterRepository repository;
    private final Pd1Client pd1Client;

    private static final Logger log = LoggerFactory.getLogger(CreditCardService.class);

    public CreditCardService(CustomerRegisterRepository repository, Pd1Client pd1Client) {
        this.repository = repository;
        this.pd1Client = pd1Client;
    }

    public Mono<CreditCard> createCreditCard(String userId, String creditCardType) {
        log.info("Request to create credit card: type={}, userId={}", creditCardType, userId);

        return pd1Client.getCreditCardTemplate(creditCardType)
                .doOnNext(template -> log.debug("Template retrieved for {}", creditCardType))
                .flatMap(template -> {
                    CreditCard creditCard = new CreditCard(
                            userId,
                            UUID.randomUUID().toString(),
                            template.creditCardType(),
                            template.creditLimit(),
                            template.fee(),
                            template.interestRate()
                    );
                    return repository.saveCreditCard(creditCard)
                            .doOnSuccess(saved -> log.info("Successfully created credit card {} for user {}", saved.getCreditCardId(), userId));
                })
                .doOnError(e -> log.error("Failed to create credit card for user {}: {}", userId, e.getMessage()));
    }

    public Flux<CreditCard> getAllCreditCardsFromUser(String userId) {
        log.info("Request to get all credit cards from user: {}", userId);
        return repository.getAllCreditCardsFromUser(userId)
                .doOnComplete(() -> log.info("Successfully retrieved credit cards from user {}", userId))
                .doOnError(e -> log.error("Failed to get all credit cards from user {}", userId));
    }

    public Mono<CreditCard> getCreditCardById(String userId, String creditCardType, String creditCardId) {

        return repository.findCreditCardById(userId, creditCardType, creditCardId)
                .doOnSuccess(card -> {
                    if (card != null) {
                        log.info("Successfully retrieved credit card by id: {}", creditCardType);
                    } else {
                        log.warn("Credit card not found for id: {}", creditCardType);
                    }
                })
                .doOnError(e -> log.error("Failed to get credit card by id {}: {}", creditCardType, e.getMessage()));

    }

    public Mono<Void> deleteCreditCardById(String userId, String creditCardType, String creditCardId) {
        log.info("Request to delete credit card by id: {}", creditCardType);
        return repository.deleteCreditCardById(userId, creditCardType, creditCardId)
                .doOnSuccess(deleted -> log.info("Successfully deleted credit card by id {}", creditCardType))
                .doOnError(e -> log.error("Failed to delete credit card by id {}", creditCardType));
    }

    public Mono<CreditCard> updateCreditCardById(String userId, String creditCardType, String creditCardId, CreditCardUpdateDTO request) {
        log.info("Request to update credit card: userId={}, type={}, cardId={}", userId, creditCardType, creditCardId);
        return repository.findCreditCardById(userId, creditCardType, creditCardId)
                .switchIfEmpty(Mono.error(new RuntimeException("Credit card not found")))
                .flatMap(existing -> {
                    log.debug("Found existing card, applying updates from DTO");
                    List<String> updates = new ArrayList<>();

                    if (request.creditLimit() != null) {
                        existing.setCreditLimit(request.creditLimit());
                        updates.add("creditLimit");
                    }
                    if (request.fee() != null) {
                        existing.setFee(request.fee());
                        updates.add("fee");
                    }
                    if (request.interestRate() != null) {
                        existing.setInterestRate(request.interestRate());
                        updates.add("interestRate");
                    }
                    if (request.status() != null) {
                        existing.setStatus(request.status());
                        updates.add("status");
                    }

                    log.info ("Updating CreditCard {} for user {}. Fields: {}", creditCardType, userId, updates);
                    log.debug("Update details for card {}: {}", creditCardType, request);
                    return repository.updateCreditCard(existing)
                            .doOnSuccess(updated -> log.info("Successfully updated credit card: id={}", creditCardId))
                            .doOnError(e -> log.error("Failed to write update to database for credit card {}: {}", creditCardId, e.getMessage()));
                })
                .doOnError(e -> log.error("Update flow failed for userId {} and card {}: {}", userId, creditCardId, e.getMessage()));
    }

}
