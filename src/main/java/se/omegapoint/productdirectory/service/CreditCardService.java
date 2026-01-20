package se.omegapoint.productdirectory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.mapper.CreditCardMapper;
import se.omegapoint.productdirectory.models.creditcards.CreditCard;
import se.omegapoint.productdirectory.repositories.CreditCardRepository;

import java.util.List;

@Service
@Transactional
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;

    // Konstruktor för injection
    public CreditCardService(CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
    }

    // Hämtar och returnerar lista med alla kreditkort
    public List<CreditCardResponseDTO> getAllCreditCards() {
        return creditCardRepository.findAll() // Repo/hibernate skapar SQL för att hämta alla kort
                .stream() // Streamar alla delar en efter en
                .map(creditCardMapper::mapToCreditCardResponse) // Mappar om från entity till DTO
                .toList(); // Lägger till i lista
    }

    // Skapar nytt kreditkort i databasen och returnerar response
    public CreditCardResponseDTO addCreditCard(CreditCardRequestDTO request) {
        CreditCard entity = creditCardMapper.mapToCreditCardEntity(request); // Använder request och mappar till entity
        CreditCard savedEntity = creditCardRepository.save(entity); // Entity sparas i databasen via repo

        return creditCardMapper.mapToCreditCardResponse(savedEntity); // Entity mappas om till response-dto och returneras
    }

    // getCreditCardById

    // updateCreditCardById

    // deleteCreditCardById


}
