package se.omegapoint.bankservice.services;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.Pd1Client;
import se.omegapoint.bankservice.dtos.CreditCardUpdateDTO;
import se.omegapoint.bankservice.dtos.LoanRequestDTO;
import se.omegapoint.bankservice.dtos.LoanUpdateDTO;
import se.omegapoint.bankservice.models.CreditCard;
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

    public Flux<Loan> getAllLoansFromUser(String userId) {

        return repository.getAllLoansFromUser(userId);
    }

    public Mono<Void> deleteLoanById(String userId, String loanType, String loanId) {

        return repository.deleteLoanById(userId, loanType, loanId);
    }

    public Mono<Loan> updateLoanById(String userId, String loanType, String loanId, LoanUpdateDTO request) {

        return repository.findLoanById(userId, loanType, loanId)
                .flatMap(existing -> {
                    if (request.loanStatus() != null) existing.setLoanStatus(request.loanStatus());
                    if (request.interestRate() != null) existing.setInterestRate(request.interestRate());
                    if (request.durationMonths() != null) existing.setDurationMonths(request.durationMonths());
                    if (request.amount() != null) existing.setAmount(request.amount());
                    return repository.updateLoan(existing);
                });
    }
}