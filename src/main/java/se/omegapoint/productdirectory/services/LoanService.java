package se.omegapoint.productdirectory.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.exceptions.ResourceNotFoundException;
import se.omegapoint.productdirectory.mappers.LoanMapper;
import se.omegapoint.productdirectory.models.loans.Loan;
import se.omegapoint.productdirectory.repositories.LoanRepository;

import java.util.List;

@Service
@Transactional
public class LoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    // Konstruktor för injection
    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    // Hämtar och returnerar alla lån
    public List<LoanResponseDTO> getAllLoans() {
        log.info("Retrieving all loans");

        return loanRepository.findAll() // Repo/hibernate skapar SQL för att hämta alla lån
                .stream() // Streamar alla delar en efter en
                .map(loanMapper::mapToLoanResponse) // Mappar om från entity till DTO
                .toList(); // Lägger till i lista
    }

    // Hämtar och returnerar specifikt lån baserat på id
    public LoanResponseDTO getLoanById(Integer id) {
        log.info("Retrieving loan by id: {}", id);

        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find loan with id: " + id));
        return loanMapper.mapToLoanResponse(existingLoan);
    }

    // Skapar nytt lån i databasen och returnerar response
    public LoanResponseDTO addLoan(LoanRequestDTO request) {
        log.info("Adding a loan: {}", request);

        Loan entity = loanMapper.mapToLoan(request); // Använder request och mappar till entity
        Loan savedEntity = loanRepository.save(entity); // Entity sparas i databasen via repo

        return loanMapper.mapToLoanResponse(savedEntity); // Entity mappas om till response-dto och returneras
    }

    // Uppdaterar specifikt lån baserat på id
    public LoanResponseDTO updateLoan(Integer id, LoanRequestDTO request) {
        log.info("Updating loan with id: {}", id);

        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id " + id + " not found"));

        loanMapper.updateEntityFromDTO(request, existingLoan); // Mappar om befintlig entity med ny data från requestDTO
        Loan savedEntity = loanRepository.save(existingLoan);

        log.info("Successfully updated loan with id: {}", id);
        return loanMapper.mapToLoanResponse(savedEntity);
    }

    // Tar bort specifikt lån baserat på id
    public void deleteLoan(Integer id) {
        log.info("Deleting a loan with id: {}", id);

        Loan existingLoan = loanRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Loan with id: " + id + "not found")
        );
        loanRepository.delete(existingLoan);
        log.info("Successfully deleted loan with id: {}", id);
    }
}
