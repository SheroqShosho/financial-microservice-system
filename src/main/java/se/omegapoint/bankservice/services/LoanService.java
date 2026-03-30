package se.omegapoint.bankservice.services;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.Pd1Client;
import se.omegapoint.bankservice.dtos.LoanRequestDTO;
import se.omegapoint.bankservice.dtos.LoanUpdateDTO;
import se.omegapoint.bankservice.models.Loan;
import se.omegapoint.bankservice.repositories.CustomerRegisterRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Singleton
public class LoanService {

    private static final Logger log =  LoggerFactory.getLogger(LoanService.class);
    private final ExchangeRateService exchangeService;
    private final CustomerRegisterRepository repository;
    private final Pd1Client pd1Client;

    public LoanService(ExchangeRateService exchangeService, CustomerRegisterRepository customerRegisterRepository, Pd1Client pd1Client) {
        this.exchangeService = exchangeService;
        this.repository = customerRegisterRepository;
        this.pd1Client = pd1Client;


    }

    public Mono<Loan> createLoan(String userId, LoanRequestDTO request) {
        log.info("Creating loan for user: {}", userId);

        String currency = request.currency() != null ? request.currency() : "SEK";

        if (currency.equals("SEK")) {
            return pd1Client.getLoanTemplate(request.loanType())
                    .doOnSuccess(template ->
                            log.info("Template fetched from PD1: type={} interestRate={}",
                                    template.loanType(), template.interestRate()))
                    .doOnError(e ->
                            log.error("Failed to fetch template from PD1: {}", e.getMessage()))
                    .flatMap(template -> {
                        Loan loan = new Loan(
                                userId,
                                UUID.randomUUID().toString(),
                                "ACTIVE",
                                template.loanType(),
                                template.interestRate(),
                                request.durationMonths(),
                                request.amount()
                        );
                        return repository.saveLoan(loan);
                    })
                    .doOnSuccess(loan ->
                            log.info("Loan created with id={}", loan.getLoanId()))
                    .doOnError(e ->
                            log.error("Failed to create loan for userId={}", userId, e));
        }

        return Mono.zip(
                        pd1Client.getLoanTemplate(request.loanType())
                                .doOnSuccess(template ->
                                        log.info("Template fetched from PD1: type={} interestRate={}",
                                                template.loanType(), template.interestRate()))
                                .doOnError(e ->
                                        log.error("Failed to fetch template from PD1: {}", e.getMessage())),
                        exchangeService.getRates(currency, "SEK")
                                .doOnSuccess(rates ->
                                        log.info("Exchange rates fetched: from={} rates={}",
                                                currency, rates.rates()))
                                .doOnError(e ->
                                        log.error("Failed to fetch exchange rates: {}", e.getMessage()))
                )
                .doOnSuccess(tuple ->
                        log.info("Both PD1 template and exchange rates fetched successfully"))
                .flatMap(tuple -> {
                    BigDecimal exchangeRate = tuple.getT2().rates().get("SEK");
                    BigDecimal amountInSek = request.amount().multiply(exchangeRate);

                    log.info("Converting {} {} to {} SEK with rate {}",
                            request.amount(), currency, amountInSek, exchangeRate);

                    Loan loan = new Loan(
                            userId,
                            UUID.randomUUID().toString(),
                            "ACTIVE",
                            tuple.getT1().loanType(),
                            tuple.getT1().interestRate(),
                            request.durationMonths(),
                            amountInSek
                    );
                    return repository.saveLoan(loan);
                })
                .doOnSuccess(loan ->
                        log.info("Loan created with id={}", loan.getLoanId()))
                .doOnError(e ->
                        log.error("Failed to create loan for userId={}", userId, e));
    }

    public Flux<Loan> getAllLoansFromUser(String userId) {
        log.info("Fetching all loans from user: {}", userId);

        return repository.getAllLoansFromUser(userId)

        .doOnComplete(() -> log.info("Loans received successfully for user: {}", userId))

                .doOnError(error -> log.error("Could not fetch loan for: {}: {}", userId, error.getMessage()));

    }

    public Mono<Loan> getLoanById(String userId, String loanType, String loanId) {
        log.info("Fetching loan for userId={}, id={}", userId, loanId);

        return repository.findLoanById(userId, loanType, loanId)

                .doOnSuccess(loan -> {
                    if (loan != null) {
                        log.info("Loan found for userId={}, id={}", userId, loanId);
                    } else {
                        log.warn("Loan not found for userId={}, id={}", userId, loanId);
                    }
                })

                .doOnError(error -> log.error("Error fetching loan for userId={}, id={}: {}", userId, loanId, error.getMessage()));
    }

    public Mono<Void> deleteLoanById(String userId, String loanType, String loanId) {
        log.info("Deleting loan: {} for user: {}" , loanId, userId);

        return repository.deleteLoanById(userId, loanType, loanId)

                .doOnSuccess(v -> log.info("Loan: {} deleted for user: {} ",  loanId, userId))

                .doOnError(error -> log.error("Could not delete loan: {}, reason: {}", loanId, error.getMessage()));
    }

    public Mono<Loan> updateLoanById(String userId, String loanType, String loanId, LoanUpdateDTO request) {
        log.info("Updating loan: {} for user: {}", loanId , userId);

        return repository.findLoanById(userId, loanType, loanId)

                .switchIfEmpty(Mono.error(new RuntimeException("Loan not found")))
                .doOnError(error -> log.warn("Update failed: {} could not be found for user: {}", loanId, userId))

                .flatMap(existing -> {log.debug("Found existing loan. Applying changes from request.");

                    if (request.loanStatus() != null) existing.setLoanStatus(request.loanStatus());
                    if (request.interestRate() != null) existing.setInterestRate(request.interestRate());
                    if (request.durationMonths() != null) existing.setDurationMonths(request.durationMonths());
                    if (request.amount() != null) existing.setAmount(request.amount());
                    return repository.updateLoan(existing)
                            .doOnSuccess(updatedLoan -> log.info("Loan {} updated successfully", loanId));
                })

                .doOnError(error -> log.error("Technical problem with update for loan {}: {}", loanId, error.getMessage()));
    }
}