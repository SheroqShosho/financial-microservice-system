package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.*;
import se.omegapoint.bankservice.mappers.LoanMapper;
import se.omegapoint.bankservice.services.LoanService;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller("/loan")
@Secured(IS_ANONYMOUS)
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(final LoanService loanService, final LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    @Post
    public Mono<HttpResponse<LoanResponseDTO>> addLoan(@Body LoanRequestDTO request) {

        String testUserId = "test123";

        return loanService.createLoan(testUserId, request)
                .map(loanMapper::toResponseDTO)
                .map(HttpResponse::created);
    }

    @Get
    public Flux<LoanResponseDTO> getAllLoans() {

        String testUserId = "test123";

        return loanService.getAllLoansFromUser(testUserId)
                .map(loanMapper::toResponseDTO);
    }

    @Put("/{userId}/{loanType}/{loanId}")
    public Mono<HttpResponse<LoanResponseDTO>> updateLoan(
            @PathVariable String userId,
            @PathVariable String loanType,
            @PathVariable String loanId,
            @Body LoanUpdateDTO request) {

        return loanService.updateLoanById(userId, loanType, loanId, request)
                .map(loanMapper::toResponseDTO)
                .map(HttpResponse::ok);
    }

    // Case sensitive på type i URL
    @Delete("/{userId}/{loanType}/{loanId}")
    public Mono<HttpResponse<Void>> deleteLoan(
            @PathVariable String userId,
            @PathVariable String loanType,
            @PathVariable String loanId) {
        return loanService.deleteLoanById(userId,loanType,loanId)
                .thenReturn(HttpResponse.noContent());
    }
}
