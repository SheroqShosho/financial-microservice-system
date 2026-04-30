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
import se.omegapoint.bankservice.dtos.*;
import se.omegapoint.bankservice.mappers.LoanMapper;
import se.omegapoint.bankservice.services.LoanService;
import se.omegapoint.bankservice.services.ProfileService;


@Controller("/loan")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class LoanController {

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);
    private final LoanService loanService;
    private final LoanMapper loanMapper;
    private final ProfileService profileService;


    public LoanController(final LoanService loanService, final LoanMapper loanMapper, final ProfileService profileService) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
        this.profileService = profileService;
    }

    @Post
    public Mono<MutableHttpResponse<LoanResponseDTO>> addLoan(
            @Body LoanApplicationDTO request,
            Authentication authentication
    ) {
        log.info("Request: Create loan with type: {} ", request.loan().loanType());

        String userId = authentication.getName();

        return profileService.getUserInformation(userId)
                .flatMap(existingProfile -> {
                    log.info("Existing profile found for userId={}, using city={}", userId, existingProfile.getCity());
                    return loanService.createLoan(userId, request.loan(), existingProfile.getCity());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    if (request.profile() == null) {
                        log.warn("No profile found and no profile data provided for userId={}", userId);
                        return Mono.error(new IllegalArgumentException("Profile information is required for first-time loan application"));
                    }
                    log.info("No existing profile for userId={}, creating new profile", userId);
                    return profileService.createProfile(userId, request.profile())
                            .flatMap(savedProfile -> loanService.createLoan(userId, request.loan(), savedProfile.getCity()));
                }))
                .map(loanMapper::toResponseDTO)
                .map(HttpResponse::created)
                .doOnSuccess(response -> log.info("Response: Loan created with status {}", response.status()))
                .doOnError(e -> log.error("Error creating loan for userId={}", userId, e));
    }


    @Get
    public Flux<LoanResponseDTO> getAllLoansFromUser(Authentication authentication) {
        log.info("Request: Get all loans");

        String userId = authentication.getName();

        return loanService.getAllLoansFromUser(userId)
                .map(loanMapper::toResponseDTO)
                .doOnComplete(() -> log.info("Response: All loans retrieved successfully"))
                .doOnError(e -> log.error("Error fetching all loans for userId={}", userId, e));
    }

    @Get("/{userId}/{loanType}/{loanId}")
    @Secured("ADMIN")
    public Mono<MutableHttpResponse<LoanResponseDTO>> getLoanById(
            @PathVariable String userId,
            @PathVariable String loanType,
            @PathVariable String loanId) {

        log.info("Request: Get loan with type: {} and id: {}", loanType, loanId);

        return loanService.getLoanById(userId, loanType, loanId)
                .map (loanMapper::toResponseDTO)
                .map(HttpResponse::ok)
                .doOnSuccess(response -> log.info("Response: Loan with id: {} retrieved successfully)", loanId))
                .doOnError(e -> log.error("Error fetching loan with id={}", loanId));
    }

    @Get("/{loanType}/{loanId}")
    public Mono<MutableHttpResponse<LoanResponseDTO>> getLoanById(
            @PathVariable String loanType,
            @PathVariable String loanId,
            Authentication authentication) {

        String userId = authentication.getName();

        log.info("Request: Get loan with type: {} and id: {}", loanType, loanId);

        return loanService.getLoanById(userId, loanType, loanId)
                .map (loanMapper::toResponseDTO)
                .map(HttpResponse::ok)
                .doOnSuccess(response -> log.info("Response: Loan with id: {} retrieved successfully)", loanId))
                .doOnError(e -> log.error("Error fetching loan with id={}", loanId));
    }

    @Put("/{userId}/{loanType}/{loanId}")
    @Secured("ADMIN")
    public Mono<MutableHttpResponse<LoanResponseDTO>> updateLoan(
            @PathVariable String userId,
            @PathVariable String loanType,
            @PathVariable String loanId,
            @Body LoanUpdateDTO request) {

        log.info("Request: Update loan: {}", loanId);

        return loanService.updateLoanById(userId, loanType, loanId, request)
                .map(loanMapper::toResponseDTO)
                .map(HttpResponse::ok)
                .doOnSuccess(response -> log.info("Response: Loan: {} updated successfully", loanId))
                .doOnError(e -> log.error("Error fetching loan with id={}", loanId));
    }

    // Case sensitive på type i URL
    @Delete("/{userId}/{loanType}/{loanId}")
    @Secured("ADMIN")
    public Mono<MutableHttpResponse<Void>> deleteLoan(
            @PathVariable String userId,
            @PathVariable String loanType,
            @PathVariable String loanId) {

        log.info("Request: Delete loan: {}", loanId);

        return loanService.deleteLoanById(userId,loanType,loanId)
                .thenReturn(HttpResponse.<Void>noContent())
                .doOnSuccess(response -> log.info("Response: Loan: {} successfully deleted", loanId))
                .doOnError(e -> log.error("Error fetching loan with id={}", loanId));
    }
}
