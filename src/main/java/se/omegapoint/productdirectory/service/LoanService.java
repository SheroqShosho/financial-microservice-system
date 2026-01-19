package se.omegapoint.productdirectory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.omegapoint.productdirectory.mapper.LoanMapper;
import se.omegapoint.productdirectory.models.loans.Loan;
import se.omegapoint.productdirectory.repositories.LoanRepository;

import java.util.List;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan addLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    public Loan getLoanById(Integer id) {
        return loanRepository.findById(id).get();
    }


}
