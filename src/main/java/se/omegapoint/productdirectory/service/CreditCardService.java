package se.omegapoint.productdirectory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
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

    // Skapar nytt kreditkort i databasen och returnerar response
    public CreditCardResponseDTO addCreditCard(CreditCardRequestDTO request) {
        log.info("Adding credit card {}", request);
        CreditCard entity = creditCardMapper.mapToCreditCardEntity(request); // Använder request och mappar till entity
        CreditCard savedEntity = creditCardRepository.save(entity); // Entity sparas i databasen via repo

        return creditCardMapper.mapToCreditCardResponse(savedEntity); // Entity mappas om till response-dto och returneras
    }

    public CreditCardResponseDTO updateCreditCard(Integer id, CreditCardRequestDTO request) {
        log.info("Updating credit card {}", request);

        CreditCard existingCreditCard = creditCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Credit card with id " + id + " not found"));
        creditCardMapper.updateEntityFromDTO(request, existingCreditCard);
        CreditCard savedEntity = creditCardRepository.save(existingCreditCard);

        return creditCardMapper.mapToCreditCardResponse(savedEntity);
    }

    public void deleteCreditCard(Integer id) {
        CreditCard existingCard = creditCardRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Credit card with id: " + id + "not found")
        );
        creditCardRepository.delete(existingCard);
    }

    // getCreditCardById
}
