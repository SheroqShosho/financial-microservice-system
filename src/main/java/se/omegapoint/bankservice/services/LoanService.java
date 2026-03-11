package se.omegapoint.bankservice.services;

import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.Pd1Client;
import se.omegapoint.bankservice.dtos.LoanRequestDTO;
import se.omegapoint.bankservice.models.Loan;
import se.omegapoint.bankservice.repositories.CustomerRegisterRepository;

import java.util.UUID;

@Singleton
public class LoanService {

    private final CustomerRegisterRepository repository;
    private final Pd1Client pd1Client;

    public LoanService(CustomerRegisterRepository customerRegisterRepository, Pd1Client pd1Client) {
        this.repository = customerRegisterRepository;
        this.pd1Client = pd1Client;

    }

    public Mono<Loan> createLoan(String userId, LoanRequestDTO request) {

        return pd1Client.getLoanTemplate(request.loanType())
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
                });
    }
}
