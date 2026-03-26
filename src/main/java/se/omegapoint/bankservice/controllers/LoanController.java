package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.*;
import se.omegapoint.bankservice.mappers.LoanMapper;
import se.omegapoint.bankservice.services.LoanService;

import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/loan")
@Secured(IS_AUTHENTICATED)
public class LoanController {

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);
    private final LoanService loanService;
    private final LoanMapper loanMapper;


    public LoanController(final LoanService loanService, final LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    @Post
    public Mono<MutableHttpResponse<LoanResponseDTO>> addLoan(
            @Body LoanRequestDTO request,
            Authentication authentication
    ) {
        log.info("Request: Create loan with type: {} ", request.loanType());

        String userId = authentication.getName();

        return loanService.createLoan(userId, request)
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

    @Put("/{userId}/{loanType}/{loanId}")
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
