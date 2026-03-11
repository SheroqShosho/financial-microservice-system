package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.LoanRequestDTO;
import se.omegapoint.bankservice.dtos.LoanResponseDTO;
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
}
