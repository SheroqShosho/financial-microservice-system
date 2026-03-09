package se.omegapoint.productdirectory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.omegapoint.productdirectory.models.loans.Loan;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    Optional<Loan> findByLoanType(String loanType);
}
