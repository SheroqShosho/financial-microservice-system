package se.omegapoint.productdirectory.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.dtos.CreditCardUpdateDTO;
import se.omegapoint.productdirectory.exceptions.ResourceNotFoundException;
import se.omegapoint.productdirectory.mappers.CreditCardMapper;
import se.omegapoint.productdirectory.models.creditcards.CreditCard;
import se.omegapoint.productdirectory.repositories.CreditCardRepository;

import java.util.List;

@Service
@Transactional
public class CreditCardService {

    private static final Logger log = LoggerFactory.getLogger(CreditCardService.class);

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;

    // Konstruktor för injection
    public CreditCardService(CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
    }

    // Hämtar och returnerar lista med alla kreditkort
    public List<CreditCardResponseDTO> getAllCreditCards() {
        log.info("Retrieving all credit cards");

        return creditCardRepository.findAll()// Repo/hibernate skapar SQL för att hämta alla kort
                .stream() // Streamar alla delar en efter en
                .map(creditCardMapper::mapToCreditCardResponse) // Mappar om från entity till DTO
                .toList(); // Lägger till i lista
    }

    // Hämtar och returnerar specifikt kreditkort baserat på id
    public CreditCardResponseDTO getCreditCardById(Integer id) {
        log.info("Retrieving credit card by id: {}", id);

        CreditCard existingCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find credit card with id: " + id));
        return creditCardMapper.mapToCreditCardResponse(existingCard);
    }

    // Skapar nytt kreditkort i databasen och returnerar response
    public CreditCardResponseDTO addCreditCard(CreditCardRequestDTO request) {
        log.info("Adding credit card {}", request);

        CreditCard entity = creditCardMapper.mapToCreditCardEntity(request); // Använder request och mappar till entity
        CreditCard savedEntity = creditCardRepository.save(entity); // Entity sparas i databasen via repo

        return creditCardMapper.mapToCreditCardResponse(savedEntity); // Entity mappas om till response-dto och returneras
    }

    // Uppdaterar specifikt kreditkort baserat på id
    public CreditCardResponseDTO updateCreditCard(Integer id, CreditCardUpdateDTO request) {
        log.info("Updating credit card with id: {}", id);

        CreditCard existingCreditCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit card with id " + id + " not found"));

        creditCardMapper.updateEntityFromDTO(request, existingCreditCard); // Mappar om befintlig entity med ny data från requestDTO
        CreditCard savedEntity = creditCardRepository.save(existingCreditCard);

        log.info("Successfully updated credit card with id: {}", id);
        return creditCardMapper.mapToCreditCardResponse(savedEntity);
    }

    // Tar bort specifikt kreditkort baserat på id
    public void deleteCreditCard(Integer id) {
        log.info("Deleting credit card by id: {}", id);

        CreditCard existingCard = creditCardRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Credit card with id: " + id + " not found")
        );

        creditCardRepository.delete(existingCard);
        log.info("Successfully deleted credit card with id: {}", id);
    }

}
