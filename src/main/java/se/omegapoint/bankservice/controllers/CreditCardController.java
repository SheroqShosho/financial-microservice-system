package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.CreditCardRequestDTO;
import se.omegapoint.bankservice.dtos.CreditCardResponseDTO;
import se.omegapoint.bankservice.dtos.CreditCardUpdateDTO;
import se.omegapoint.bankservice.mappers.CreditCardMapper;
import se.omegapoint.bankservice.services.CreditCardService;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller("/creditcard")
@Secured(IS_ANONYMOUS)
public class CreditCardController {

    private static final Logger log = LoggerFactory.getLogger(CreditCardController.class);

    private final CreditCardService creditCardService;
    private final CreditCardMapper creditCardMapper;

    public CreditCardController(CreditCardService creditCardService, CreditCardMapper creditCardMapper) {
        this.creditCardService = creditCardService;
        this.creditCardMapper = creditCardMapper;
    }

    @Post
    public Mono<MutableHttpResponse<CreditCardResponseDTO>> addCreditCard(@Body CreditCardRequestDTO request) {

        String testUserId = "test123";

        log.info("Creating credit card for userId: {}, type: {}", testUserId, request.creditCardType());

        return creditCardService.createCreditCard(testUserId, request.creditCardType())
                .map(creditCardMapper::toResponseDto)
                .map(HttpResponse::created)
                .doOnSuccess(res ->
                        log.info("Successfully created credit card for userId: {}", testUserId)
                )
                .doOnError(error ->
                        log.error("Error creating credit card for userId: {}", testUserId, error)
                );
    }

    @Get
    public Flux<CreditCardResponseDTO> getAllCreditCards() {

        String testUserId = "test123";

        log.info("Retrieving all credit cards for userId: {}", testUserId);

        return creditCardService.getAllCreditCardsFromUser(testUserId)
                .map(creditCardMapper::toResponseDto)
                .doOnComplete(()->
                            log.info("Finished retrieving credit cards for userId: {}", testUserId)
                        )
                .doOnError(error ->
                            log.error("Error retrieving credit cards for userId: {}", testUserId, error)
                        );

    }

    @Put("/{userId}/{creditCardType}/{creditCardId}")
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
