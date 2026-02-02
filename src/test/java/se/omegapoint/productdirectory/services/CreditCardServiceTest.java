package se.omegapoint.productdirectory.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.mappers.CreditCardMapper;
import se.omegapoint.productdirectory.models.creditcards.CreditCard;
import se.omegapoint.productdirectory.repositories.CreditCardRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Credit Card Services Test")
class CreditCardServiceTest {

    @InjectMocks
    CreditCardService creditCardService;

    @Mock
    CreditCardRepository creditCardRepository;

    @Mock
    CreditCardMapper creditCardMapper;


    @Test
    @DisplayName("Hämtar alla Creditcard")
    public void getAllCreditCardsShouldReturnAllMappedCreditCards() {

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
    void addCreditCardShouldAddCreditCard() {
        CreditCardRequestDTO request = mock(CreditCardRequestDTO.class);

        CreditCard card = new CreditCard();

        CreditCard savedCard = new CreditCard();

        CreditCardResponseDTO response = mock(CreditCardResponseDTO.class);

    }

    @Test
    void updateCreditCardShouldUpdateCreditCardById() {
    }

    @Test
    void deleteCreditCardShouldDeleteCreditCardById() {
    }
}