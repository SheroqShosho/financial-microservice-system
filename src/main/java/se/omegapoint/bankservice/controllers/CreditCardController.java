package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.CreditCardApplicationDTO;
import se.omegapoint.bankservice.dtos.CreditCardResponseDTO;
import se.omegapoint.bankservice.dtos.CreditCardUpdateDTO;
import se.omegapoint.bankservice.mappers.CreditCardMapper;
import se.omegapoint.bankservice.services.CreditCardService;
import se.omegapoint.bankservice.services.ProfileService;

@Controller("/creditcard")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class CreditCardController {

    private static final Logger log = LoggerFactory.getLogger(CreditCardController.class);

    private final CreditCardService creditCardService;
    private final CreditCardMapper creditCardMapper;
    private final ProfileService profileService;

    public CreditCardController(CreditCardService creditCardService, CreditCardMapper creditCardMapper, ProfileService profileService) {
        this.creditCardService = creditCardService;
        this.creditCardMapper = creditCardMapper;
        this.profileService = profileService;
    }

    @Post
    public Mono<MutableHttpResponse<CreditCardResponseDTO>> addCreditCardWithProfile(
            @Body CreditCardApplicationDTO request,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        if (request == null || request.creditcard() == null) {
            log.warn("CreditCardApplicationDTO or creditcard is null for userId={}", userId);
            return Mono.error(new IllegalArgumentException("Credit card information is required"));
        }
        return profileService.getUserInformation(userId)
                .flatMap(existingProfile -> {
                    log.info("Existing profile found for userId={}", userId);
                    return creditCardService.createCreditCard(userId, request.creditcard().creditCardType());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    if (request.profile() == null) {
                        log.warn("No profile found and no profile data provided for userId={}", userId);
                        return Mono.error(new IllegalArgumentException("Profile information is required for first-time credit card application"));
                    }
                    log.info("No existing profile for userId={}, creating new profile", userId);
                    return profileService.createProfile(userId, request.profile())
                            .flatMap(savedProfile -> creditCardService.createCreditCard(userId, request.creditcard().creditCardType()));
                }))
                .map(creditCardMapper::toResponseDto)
                .map(HttpResponse::created)
                .doOnSuccess(response -> log.info("Response: Credit card created with status {}", response.status()))
                .doOnError(e -> log.error("Error creating credit card for userId={}", userId, e));
    }

    @Get
    public Flux<CreditCardResponseDTO> getAllCreditCardsFromUser(Authentication authentication) {

        String userId = authentication.getName();

        log.info("Retrieving all credit cards for userId: {}", userId);

        return creditCardService.getAllCreditCardsFromUser(userId)
                .map(creditCardMapper::toResponseDto)
                .doOnComplete(()->
                            log.info("Finished retrieving credit cards for userId: {}", userId)
                        )
                .doOnError(error ->
                            log.error("Error retrieving credit cards for userId: {}", userId, error)
                        );

    }

    @Get("/{userId}/{creditCardType}/{creditCardId}")
    @Secured("ADMIN")
    public Mono<CreditCardResponseDTO> getCreditCardById(
            @PathVariable String userId,
            @PathVariable String creditCardType,
            @PathVariable String creditCardId) {

        log.info("Retrieving credit card for userId: {}", userId);

        return creditCardService.getCreditCardById(userId, creditCardType, creditCardId)
                .map(creditCardMapper::toResponseDto)
                .doOnSuccess(res ->
                            log.info("Successfully retrieved credit card with id: {}", creditCardId)
                        )
                .doOnError(error ->
                            log.error("Error retrieving credit card with id: {}", creditCardId, error)
                        );

    }

    @Get("/{creditCardType}/{creditCardId}")
    public Mono<CreditCardResponseDTO> getCreditCardById(
            @PathVariable String creditCardType,
            @PathVariable String creditCardId,
            Authentication authentication) {

        String userId = authentication.getName();

        log.info("Retrieving credit card for userId: {}", userId);

        return creditCardService.getCreditCardById(userId, creditCardType, creditCardId)
                .map(creditCardMapper::toResponseDto)
                .doOnSuccess(res ->
                        log.info("Successfully retrieved credit card with id: {}", creditCardId)
                )
                .doOnError(error ->
                        log.error("Error retrieving credit card with id: {}", creditCardId, error)
                );

    }

    @Put("/{userId}/{creditCardType}/{creditCardId}")
    @Secured("ADMIN")
    public Mono<MutableHttpResponse<CreditCardResponseDTO>> updateCreditCard(
            @PathVariable String userId,
            @PathVariable String creditCardType,
            @PathVariable String creditCardId,
            @Body CreditCardUpdateDTO request) {

        log.info("Updating credit card for userId: {}, type: {}, id: {}",
                userId, creditCardType, creditCardId);


        return creditCardService.updateCreditCardById(userId, creditCardType, creditCardId, request)
                .map(creditCardMapper::toResponseDto)
                .map(HttpResponse::ok)
                .doOnSuccess(res ->
                            log.info("Successfully updated credit card with id: {}", creditCardId)
                        )
                .doOnError(error ->
                            log.error("Error updating credit card with id: {}", creditCardId, error)
                        );

    }

    // Case sensitive på type i URL
    @Delete("/{userId}/{creditCardType}/{creditCardId}")
    @Secured("ADMIN")
    public Mono<MutableHttpResponse<Void>> deleteCreditCard(
            @PathVariable String userId,
            @PathVariable String creditCardType,
            @PathVariable String creditCardId) {

        log.info("Deleting credit card with id: {} for userId: {}", creditCardId, userId);

        return creditCardService.deleteCreditCardById(userId,creditCardType,creditCardId)
                .thenReturn(HttpResponse.<Void>noContent())
                .doOnSuccess(res ->
                        log.info("Successfully deleted credit card with id: {}", creditCardId)
                )
                .doOnError(error ->
                            log.error("Error deleting credit card with id: {}", creditCardId, error)
                        );
    }
}
