package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.MyPagesResponseDTO;
import se.omegapoint.bankservice.mappers.CreditCardMapper;
import se.omegapoint.bankservice.mappers.LoanMapper;
import se.omegapoint.bankservice.mappers.ProfileMapper;
import se.omegapoint.bankservice.services.CreditCardService;
import se.omegapoint.bankservice.services.LoanService;
import se.omegapoint.bankservice.services.ProfileService;

import java.util.List;


@Controller("/mypages")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MyPagesController {

    private static final Logger LOG = LoggerFactory.getLogger(MyPagesController.class);

    private final LoanService loanService;
    private final CreditCardService creditCardService;
    private final ProfileService profileService;
    private final LoanMapper loanMapper;
    private final CreditCardMapper creditCardMapper;
    private final ProfileMapper profileMapper;

    public MyPagesController(LoanService loanService, CreditCardService creditCardService, ProfileService profileService,
                             LoanMapper loanMapper, CreditCardMapper creditCardMapper, ProfileMapper profileMapper) {

        this.loanService = loanService;
        this.creditCardService = creditCardService;
        this.profileService = profileService;
        this.loanMapper = loanMapper;
        this.creditCardMapper = creditCardMapper;
        this.profileMapper = profileMapper;
    }

    @Get
    public Mono<MutableHttpResponse<MyPagesResponseDTO>> getMyPages(Authentication authentication) {
        String userId = authentication.getName();

        String firstName = (String) authentication.getAttributes().getOrDefault("firstName", "");
        String lastName = (String) authentication.getAttributes().getOrDefault("lastName", "");

        return Mono.zip(profileService.getOrCreateProfile(userId, firstName, lastName)
                .onErrorResume(e -> {
                    LOG.error("Profile fetch failed for userId={}", userId, e);
                    return Mono.empty();
                }),

                loanService.getAllLoansFromUser(userId)
                        .collectList()
                        .onErrorResume(e -> {
                            LOG.error("Loan fetch failed for userId={}", userId, e);
                            return Mono.just(List.of());
                        }),

                creditCardService.getAllCreditCardsFromUser(userId)
                        .collectList()
                        .onErrorResume(e -> {
                            LOG.error("Creditcard fetch failed for userId={}", userId, e);
                            return Mono.just(List.of());
                        })
        )
                .map(tuple -> new MyPagesResponseDTO(
                        profileMapper.toResponseDto(tuple.getT1()),
                        tuple.getT2().stream().map(loanMapper::toResponseDTO).toList(),
                        tuple.getT3().stream().map(creditCardMapper::toResponseDto).toList()
                ))
                .map(HttpResponse::ok)
                .doOnSuccess(response ->
                    LOG.info("Successfully retrieved MyPages for userId={}", userId))
                .onErrorResume(e -> {
                    LOG.error("Error retrieving MyPages for userId={}", userId, e);
                    return Mono.just(HttpResponse.serverError());
                });



    }

    @Get("/{userId}")
//    @Secured("ADMIN")
    public Mono<MutableHttpResponse<MyPagesResponseDTO>> getUserPages(@PathVariable String userId) {

        return Mono.zip(profileService.getUserInformation(userId)
                                .onErrorResume(e -> {
                                    LOG.error("Profile fetch failed for userId={}", userId, e);
                                    return Mono.empty();
                                }),

                        loanService.getAllLoansFromUser(userId)
                                .collectList()
                                .onErrorResume(e -> {
                                    LOG.error("Loan fetch failed for userId={}", userId, e);
                                    return Mono.just(List.of());
                                }),

                        creditCardService.getAllCreditCardsFromUser(userId)
                                .collectList()
                                .onErrorResume(e -> {
                                    LOG.error("Creditcard fetch failed for userId={}", userId, e);
                                    return Mono.just(List.of());
                                })
                )
                .map(tuple -> new MyPagesResponseDTO(
                        profileMapper.toResponseDto(tuple.getT1()),
                        tuple.getT2().stream().map(loanMapper::toResponseDTO).toList(),
                        tuple.getT3().stream().map(creditCardMapper::toResponseDto).toList()
                ))
                .map(HttpResponse::ok)
                .doOnSuccess(response ->
                        LOG.info("Successfully retrieved MyPages for userId={}", userId))
                .onErrorResume(e -> {
                    LOG.error("Error retrieving MyPages for userId={}", userId, e);
                    return Mono.just(HttpResponse.serverError());
                });
    }
}
