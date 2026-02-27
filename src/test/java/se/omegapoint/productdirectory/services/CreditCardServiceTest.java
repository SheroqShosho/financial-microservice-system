package se.omegapoint.productdirectory.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.dtos.CreditCardUpdateDTO;
import se.omegapoint.productdirectory.exceptions.ResourceNotFoundException;
import se.omegapoint.productdirectory.mappers.CreditCardMapper;
import se.omegapoint.productdirectory.models.creditcards.CreditCard;
import se.omegapoint.productdirectory.repositories.CreditCardRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Credit Card Services Test")
class CreditCardServiceTest {

    @Mock
    CreditCardRepository creditCardRepository;

    @Mock
    CreditCardMapper creditCardMapper;

    @InjectMocks
    CreditCardService creditCardService;


    @Test
    @DisplayName("Hämtar alla Creditcard")
    void getAllCreditCardsShouldReturnAllMappedCreditCards() {

        //ARRANGE

        //Skapar två creditcard entity
        CreditCard card1 = new CreditCard();
        CreditCard card2 = new CreditCard();

        // Mocka DTO-objekt
        CreditCardResponseDTO dto1 = mock(CreditCardResponseDTO.class);
        CreditCardResponseDTO dto2 = mock(CreditCardResponseDTO.class);

        //Skapar en lista med båda entity
        List<CreditCard> cards = List.of(card1, card2);


        // Sätter vad mockarna ska returnera
        when(creditCardRepository.findAll()).thenReturn(cards);
        when(creditCardMapper.mapToCreditCardResponse(card1)).thenReturn(dto1);
        when(creditCardMapper.mapToCreditCardResponse(card2)).thenReturn(dto2);

        //ACT

        // Anropar metoden
        List<CreditCardResponseDTO> result = creditCardService.getAllCreditCards();

        //ASSERT

        // Resultatet får inte retunera null
        assertNotNull(result);

        // Kontrollerar att listan innehåller två objekt
        assertEquals(2, result.size());

        // Kontrollerar att listan innehåller dto-objekt
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));

        // Verifierar att findAll() körs en gång
        verify(creditCardRepository, times(1)).findAll();

        // Verifierar att båda creditcard entity mappas om till DTO
        verify(creditCardMapper, times(1)).mapToCreditCardResponse(card1);
        verify(creditCardMapper, times(1)).mapToCreditCardResponse(card2);

        verifyNoMoreInteractions(creditCardRepository, creditCardMapper);

    }

    @Test
    @DisplayName("Hämtar och retunerar Creditcard med specifik ID")
    void getCreditCardByIdShouldReturnMappedCreditCardById() {

        //ARRANGE

        // Id:et för vår test
        Integer id = 1;

        // Skapar Creditcard
        CreditCard card = new CreditCard();

        // Mockar response-DTO som mapparen ska retunera
        CreditCardResponseDTO response = mock(CreditCardResponseDTO.class);

        // Repository hämtar kortet med hjälp av Id från db
        when(creditCardRepository.findById(id)).thenReturn(Optional.of(card));

        // Entity mappas om till dto och returnerar response
        when(creditCardMapper.mapToCreditCardResponse(card)).thenReturn(response);

        //ACT

        // Anropar metoden
        CreditCardResponseDTO result = creditCardService.getCreditCardById(id);

        //ASSERT

        // Resultatet får inte retunera null
        assertNotNull(result);

        // Kontrollerar att rätt dto retuneras
        assertEquals(response, result);

        // Verifierar att findById körs en gång och att det anropas med rätt id
        verify(creditCardRepository, times(1)).findById(id);

        // Verifierar att entity mappas till dto och körs en gång
        verify(creditCardMapper, times(1)).mapToCreditCardResponse(card);

        // Inga mer verifieringar/anrop sker
        verifyNoMoreInteractions(creditCardRepository, creditCardMapper);

    }

    @Test
    @DisplayName("Adderar Credit card och sparar den till db samt returnerar response ")
    void addCreditCardShouldMapSaveAndReturnMappedDTO() {

        //ARRANGE

        // Mockar Request-dto
        CreditCardRequestDTO request = mock(CreditCardRequestDTO.class);

        // Skapar Creditcard
        CreditCard card = new CreditCard();

        // Skapar entity som repository ska returnera
        CreditCard savedCard = new CreditCard();

        // Mockar response-dto
        CreditCardResponseDTO response = mock(CreditCardResponseDTO.class);

        //Mappar till ett CreditCarrd entity
        when(creditCardMapper.mapToCreditCardEntity(request)).thenReturn(card);

        // Repository sparar entity och returnerar den sparade versionen
        when(creditCardRepository.save(card)).thenReturn(savedCard);

        // Mappar sparat entity till response-dto
        when(creditCardMapper.mapToCreditCardResponse(savedCard)).thenReturn(response);

        //ACT

        // Anroper metoden som ska testas
        CreditCardResponseDTO result = creditCardService.addCreditCard(request);

        //ASSERT

        //Kontrollerar att resultatet inte är null
        assertNotNull(result);

        //Kontrollerar att rätt response retuneras
        assertEquals(response, result);

        // Verifiera att request mappas till entity
        verify(creditCardMapper, times(1)).mapToCreditCardEntity(request);

        //Verifierar att entity sparas i repository
        verify(creditCardRepository, times(1)).save(card);

        //Verifierar att sparad entity mappas till response-dto
        verify(creditCardMapper, times(1)).mapToCreditCardResponse(savedCard);

        // Säkerställer att inga extra anrop görs
        verifyNoMoreInteractions(creditCardMapper, creditCardRepository);


    }

    @Test
    @DisplayName("Hittar,uppdaterar och sparar Credit card by id. Returnerar dto ")
    void updateCreditCardByIdShouldUpdateCreditCardByIdAndReturnDTO() {

        //ARRANGE

        Integer id = 1;

        // Mockar update-dto
        CreditCardUpdateDTO request = mock(CreditCardUpdateDTO.class);

        //Existerande kreditcard
        CreditCard existingCard = new CreditCard();

        //Sparad entity efter uppdatering
        CreditCard savedCard = new CreditCard();

        // Mockar response-dto
        CreditCardResponseDTO response = mock(CreditCardResponseDTO.class);

        //Repository hittar kortet
        when(creditCardRepository.findById(id)).thenReturn(Optional.of(existingCard));

        // Repository sparar och returnerar uppdaterad entity
        when(creditCardRepository.save(existingCard)).thenReturn(savedCard);

        // Mappar sparad entity till response-dto
        when(creditCardMapper.mapToCreditCardResponse(savedCard)).thenReturn(response);

        //ACT

        CreditCardResponseDTO result = creditCardService.updateCreditCard(id, request);

        //ASSERT

        assertNotNull(result);
        assertEquals(response, result);

        // Verifierar att kortet hämtas via id
        verify(creditCardRepository, times(1)).findById(id);

        //Verifierar att befintlig entity uppdatera med data från request
        verify(creditCardMapper, times(1)).updateEntityFromDTO(request, existingCard);

        //Verifierar att uppdaterad entity sparas
        verify(creditCardRepository, times(1)).save(existingCard);

        //Verifierar att sparad entity mappas till response-dto
        verify(creditCardMapper, times(1)).mapToCreditCardResponse(savedCard);

        // Säkerställer att inga extra anrop görs
        verifyNoMoreInteractions(creditCardRepository, creditCardMapper);
    }

    @Test
    @DisplayName("Kastar en Exception när card by id inte hittas")
    void updateCreditCardShouldThrowResourceNotFoundExceptionWhenNotFound() {

        //ARRANGE

        Integer id = 99;
        CreditCardUpdateDTO request = mock(CreditCardUpdateDTO.class);

        when(creditCardRepository.findById(id)).thenReturn(Optional.empty());

        //ACT + ASSERT
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> creditCardService.updateCreditCard(id, request)
        );

        assertEquals("Credit card with id " + id + " not found", ex.getMessage());

        // Verifierar att repository anropas
        verify(creditCardRepository, times(1)).findById(id);

        //Mapper och save ska int anropas när inget hittas
        verifyNoMoreInteractions(creditCardMapper, creditCardRepository);

        verifyNoMoreInteractions(creditCardRepository);

    }

    @Test
    @DisplayName("Raderar Credit card by id")
    void deleteCreditCardShouldDeleteCreditCardById() {

        //ARRANGE

        Integer id = 1;
        CreditCard card = new CreditCard();

        // Repository hittar kreditkort
        when(creditCardRepository.findById(id)).thenReturn(Optional.of(card));

        //ACT

        creditCardService.deleteCreditCard(id);

        //ASSERT

        // Verifierar att repository hämtar kortet
        verify(creditCardRepository, times(1)).findById(id);

        // Verifierar att repository tar bort rätt entity
        verify(creditCardRepository, times(1)).delete(card);

        // Säkerställer att inga extra anrop görs
        verifyNoMoreInteractions(creditCardRepository);
    }

    @Test
    @DisplayName("Kastar en Exception när card by id inte hittas")
    void deleteCreditCardShouldThrowResourceNotFoundExceptionWhenNotFound() {

        //ARRANGE
        Integer id = 77;

        when(creditCardRepository.findById(id)).thenReturn(Optional.empty());

        // ACT + ASSERT

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> creditCardService.deleteCreditCard(id)
        );

        assertEquals(
                "Credit card with id: " + id + " not found",
                ex.getMessage()
        );

        // Verifierar att repository anropas
        verify(creditCardRepository, times(1)).findById(id);

        // delete ska inte anropas om inget hittas
        verify(creditCardRepository, never()).delete(any());

        verifyNoMoreInteractions(creditCardRepository);

    }
}