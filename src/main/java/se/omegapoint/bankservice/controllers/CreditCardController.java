package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.CreditCardRequestDTO;
import se.omegapoint.bankservice.dtos.CreditCardResponseDTO;
import se.omegapoint.bankservice.mappers.CreditCardMapper;
import se.omegapoint.bankservice.services.CreditCardService;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller("/creditcard")
@Secured(IS_ANONYMOUS)
public class CreditCardController {

    private final CreditCardService creditCardService;
    private final CreditCardMapper creditCardMapper;

    public CreditCardController(CreditCardService creditCardService, CreditCardMapper creditCardMapper) {
        this.creditCardService = creditCardService;
        this.creditCardMapper = creditCardMapper;
    }

    @Post
    public Mono<HttpResponse<CreditCardResponseDTO>> addCreditCard(@Body CreditCardRequestDTO request) {

        String testUserId = "test123";

        return creditCardService.createCreditCard(testUserId, request.creditCardType())
                .map(creditCardMapper::toResponseDto)
                .map(HttpResponse::created);
    }

    @Get
    public Flux<CreditCardResponseDTO> getAllCreditCards() {

        String testUserId = "test123";

        return creditCardService.getAllCreditCardsFromUser(testUserId)
                .map(creditCardMapper::toResponseDto);

    }

    // Case sensitive på type i URL
    @Delete("/{userId}/{creditCardType}/{creditCardId}")
    public Mono<HttpResponse<Void>> deleteCreditCard(
            @PathVariable String userId,
            @PathVariable String creditCardType,
            @PathVariable String creditCardId) {
        return creditCardService.deleteCreditCardById(userId,creditCardType,creditCardId)
                .thenReturn(HttpResponse.noContent());
    }
}
