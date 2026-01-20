package se.omegapoint.productdirectory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.mapper.LoanMapper;
import se.omegapoint.productdirectory.models.loans.Loan;
import se.omegapoint.productdirectory.repositories.LoanRepository;

import java.util.List;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    // Konstruktor för injection
    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    // Hämtar och returnerar alla lån
    public List<LoanResponseDTO> getAllLoans() {
        return loanRepository.findAll() // Repo/hibernate skapar SQL för att hämta alla lån
                .stream() // Streamar alla delar en efter en
                .map(loanMapper::mapToLoanResponse) // Mappar om från entity till DTO
                .toList(); // Lägger till i lista
    }

    // Skapar nytt lån i databasen och returnerar response
    public LoanResponseDTO addLoan(LoanRequestDTO request) {
        Loan entity = loanMapper.mapToLoan(request); // Använder request och mappar till entity
        Loan savedEntity = loanRepository.save(entity); // Entity sparas i databasen via repo

        return loanMapper.mapToLoanResponse(savedEntity); // Entity mappas om till response-dto och returneras
    }

    // getLoanById

    // updateLoanById

    // deleteLoanById
}
