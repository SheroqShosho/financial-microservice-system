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

import java.util.UUID;

@Singleton
public class LoanService {

    private static final Logger log =  LoggerFactory.getLogger(LoanService.class);
    private final CustomerRegisterRepository repository;
    private final Pd1Client pd1Client;

    public LoanService(CustomerRegisterRepository customerRegisterRepository, Pd1Client pd1Client) {
        this.repository = customerRegisterRepository;
        this.pd1Client = pd1Client;

    }

    public Mono<Loan> createLoan(String userId, LoanRequestDTO request) {
        log.info("Skapar lån för användare: {}", userId);

        return pd1Client.getLoanTemplate(request.loanType())
                .doOnNext(template -> log.debug("Mall hämtad"))

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
                    log.debug("Sparar lån till DynamoDB för userId {}", userId);

                    return repository.saveLoan(loan)

                            .doOnSuccess(savedLoan -> log.info("Lån skapat och sparat! ID: {}",  savedLoan.getLoanId()));
                })

                .doOnError(error -> log.error("Kunde inte skapa lån för {}: {}", userId, error.getMessage()));
    }

    public Flux<Loan> getAllLoansFromUser(String userId) {
        log.info("Hämtar alla lån från användare: {}", userId);

        return repository.getAllLoansFromUser(userId)

        .doOnComplete(() -> log.info("Lån hämtade framgångsrikt för användare: {}", userId))

                .doOnError(error -> log.error("Kunde inte hämta lån för {}: {}", userId, error.getMessage()));

    }

    public Mono<Void> deleteLoanById(String userId, String loanType, String loanId) {
        log.info("Tar bort lån: {} för användare: {}" , loanId, userId);

        return repository.deleteLoanById(userId, loanType, loanId)

                .doOnSuccess(v -> log.info("Lån: {} raderat för användare: {} ",  loanId, userId))

                .doOnError(error -> log.error("Kunde inte radera lån: {}, orsak: {}", loanId, error.getMessage()));
    }

    public Mono<Loan> updateLoanById(String userId, String loanType, String loanId, LoanUpdateDTO request) {
        log.info("Uppdaterar lån: {} för användare: {}", loanId , userId);

        return repository.findLoanById(userId, loanType, loanId)

                .switchIfEmpty(Mono.error(new RuntimeException("Lånet hittades inte")))
                .doOnError(error -> log.warn("Update misslyckades: {} hittades inte för användare: {}", loanId, userId))

                .flatMap(existing -> {log.debug("Hittade befintligt lån. Applicerar ändringar från request.");

                    if (request.loanStatus() != null) existing.setLoanStatus(request.loanStatus());
                    if (request.interestRate() != null) existing.setInterestRate(request.interestRate());
                    if (request.durationMonths() != null) existing.setDurationMonths(request.durationMonths());
                    if (request.amount() != null) existing.setAmount(request.amount());
                    return repository.updateLoan(existing)
                            .doOnSuccess(updatedLoan -> log.info("Lån {} uppdaterat framgångsrikt", loanId));
                })

                .doOnError(error -> log.error("Tekniskt fel vid uppdatering av lån {}: {}", loanId, error.getMessage()));
    }
}