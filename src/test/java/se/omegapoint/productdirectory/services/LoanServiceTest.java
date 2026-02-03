package se.omegapoint.productdirectory.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.exceptions.ResourceNotFoundException;
import se.omegapoint.productdirectory.mappers.LoanMapper;
import se.omegapoint.productdirectory.models.loans.Loan;
import se.omegapoint.productdirectory.repositories.LoanRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName(" Loan Services Test")
class LoanServiceTest {

    @InjectMocks
    LoanService loanService;

    @Mock
    LoanRepository loanRepository;

    @Mock
    LoanMapper loanMapper;


    @Test
    @DisplayName("Hämtar alla Loan")
    void getAllLoansShouldReturnAllMappedLoans() {

        //ARRANGE

        //Skapar två loan entity
        Loan loan1 = new Loan();
        Loan loan2 = new Loan();

        // Mocka DTO-objekt
        LoanResponseDTO dto1 = mock(LoanResponseDTO.class);
        LoanResponseDTO dto2 = mock(LoanResponseDTO.class);

        //Skapar en lista med båda entity
        List<Loan> loans = List.of(loan1, loan2);


        // Sätter vad mockarna ska returnera
        when(loanRepository.findAll()).thenReturn(loans);
        when(loanMapper.mapToLoanResponse(loan1)).thenReturn(dto1);
        when(loanMapper.mapToLoanResponse(loan2)).thenReturn(dto2);

        //ACT

        // Anropar metoden
        List<LoanResponseDTO> result = loanService.getAllLoans();

        //ASSERT

        // Resultatet får inte retunera null
        assertNotNull(result);

        // Kontrollerar att listan innehåller två objekt
        assertEquals(2, result.size());

        // Kontrollerar att listan innehåller dto-objekt
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));

        // Verifierar att findAll() körs en gång
        verify(loanRepository, times(1)).findAll();

        // Verifierar att båda loan entity mappas om till DTO
        verify(loanMapper, times(1)).mapToLoanResponse(loan1);
        verify(loanMapper, times(1)).mapToLoanResponse(loan2);

        verifyNoMoreInteractions(loanRepository, loanMapper);

    }

    @Test
    @DisplayName("Hämtar och retunerar Loan med specifik ID")
    void getLoanByIdShouldReturnMappedLoanById() {

        //ARRANGE

        // Id:et för vår test
        Integer id = 1;

        // Skapar Loan
        Loan loan = new Loan();

        // Mockar response-DTO som mapparen ska retunera
        LoanResponseDTO response = mock(LoanResponseDTO.class);

        // Repository hämtar kortet med hjälp av Id från db
        when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        // Entity mappas om till dto och returnerar response
        when(loanMapper.mapToLoanResponse(loan)).thenReturn(response);

        //ACT

        // Anropar metoden
        LoanResponseDTO result = loanService.getLoanById(id);

        //ASSERT

        // Resultatet får inte retunera null
        assertNotNull(result);

        // Kontrollerar att rätt dto retuneras
        assertEquals(response, result);

        // Verifierar att findById körs en gång och att det anropas med rätt id
        verify(loanRepository, times(1)).findById(id);

        // Verifierar att entity mappas till dto och körs en gång
        verify(loanMapper, times(1)).mapToLoanResponse(loan);

        // Inga mer verifieringar/anrop sker
        verifyNoMoreInteractions(loanRepository, loanMapper);

    }

    @Test
    @DisplayName("Adderar  loan och sparar den till db samt returnerar response ")
    void addLoanShouldMapSaveAndReturnMappedDTO() {

        //ARRANGE

        // Mockar Request-dto
        LoanRequestDTO request = mock(LoanRequestDTO.class);

        // Skapar loan
        Loan loan = new Loan();

        // Skapar entity som repository ska returnera
        Loan savedLoan = new Loan();

        // Mockar response-dto
        LoanResponseDTO response = mock(LoanResponseDTO.class);

        //Mappar till ett loan entity
        when(loanMapper.mapToLoan(request)).thenReturn(loan);

        // Repository sparar entity och returnerar den sparade versionen
        when(loanRepository.save(loan)).thenReturn(savedLoan);

        // Mappar sparat entity till response-dto
        when(loanMapper.mapToLoanResponse(savedLoan)).thenReturn(response);

        //ACT

        // Anroper metoden som ska testas
        LoanResponseDTO result = loanService.addLoan(request);

        //ASSERT

        //Kontrollerar att resultatet inte är null
        assertNotNull(result);

        //Kontrollerar att rätt response retuneras
        assertEquals(response, result);

        // Verifiera att request mappas till entity
        verify(loanMapper, times(1)).mapToLoan(request);

        //Verifierar att entity sparas i repository
        verify(loanRepository, times(1)).save(loan);

        //Verifierar att sparad entity mappas till response-dto
        verify(loanMapper, times(1)).mapToLoanResponse(savedLoan);

        // Säkerställer att inga extra anrop görs
        verifyNoMoreInteractions(loanMapper, loanRepository);


    }

    @Test
    @DisplayName("Hittar,uppdaterar och sparar  loan by id. Returnerar dto ")
    void updateLoanByIdShouldUpdateLoanByIdAndReturnDTO() {

        //ARRANGE

        Integer id = 1;

        // Mockar request-dto
        LoanRequestDTO request = mock(LoanRequestDTO.class);

        //Existerande loan
        Loan existingLoan = new Loan();

        //Sparad entity efter uppdatering
        Loan savedLoan = new Loan();

        // Mockar response-dto
        LoanResponseDTO response = mock(LoanResponseDTO.class);

        //Repository hittar kortet
        when(loanRepository.findById(id)).thenReturn(Optional.of(existingLoan));

        // Repository sparar och returnerar uppdaterad entity
        when(loanRepository.save(existingLoan)).thenReturn(savedLoan);

        // Mappar sparad entity till response-dto
        when(loanMapper.mapToLoanResponse(savedLoan)).thenReturn(response);

        //ACT

        LoanResponseDTO result = loanService.updateLoan(id, request);

        //ASSERT

        assertNotNull(result);
        assertEquals(response, result);

        // Verifierar att kortet hämtas via id
        verify(loanRepository, times(1)).findById(id);

        //Verifierar att befintlig entity uppdatera med data från request
        verify(loanMapper, times(1)).updateEntityFromDTO(request, existingLoan);

        //Verifierar att uppdaterad entity sparas
        verify(loanRepository, times(1)).save(existingLoan);

        //Verifierar att sparad entity mappas till response-dto
        verify(loanMapper, times(1)).mapToLoanResponse(savedLoan);

        // Säkerställer att inga extra anrop görs
        verifyNoMoreInteractions(loanRepository, loanMapper);
    }

    @Test
    @DisplayName("Kastar en Exception när loan by id inte hittas")
    void updateLoanShouldThrowResourceNotFoundExceptionWhenNotFound() {

        //ARRANGE

        Integer id = 99;
        LoanRequestDTO request = mock(LoanRequestDTO.class);

        when(loanRepository.findById(id)).thenReturn(Optional.empty());

        //ACT + ASSERT
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> loanService.updateLoan(id, request)
        );

        assertEquals("Loan with id " + id + " not found", ex.getMessage());

        // Verifierar att repository anropas
        verify(loanRepository, times(1)).findById(id);

        //Mapper och save ska int anropas när inget hittas
        verifyNoMoreInteractions(loanMapper, loanRepository);

        verifyNoMoreInteractions(loanRepository);

    }

    @Test
    @DisplayName("Raderar loan by id")
    void deleteLoanShouldDeleteLoanById() {

        //ARRANGE

        Integer id = 1;
        Loan loan =  new Loan();

        // Repository hittar loan
        when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        //ACT

        loanService.deleteLoan(id);

        //ASSERT

        // Verifierar att repository hämtar loan
        verify(loanRepository, times(1)).findById(id);

        // Verifierar att repository tar bort rätt entity
        verify(loanRepository, times(1)).delete(loan);

        // Säkerställer att inga extra anrop görs
        verifyNoMoreInteractions(loanRepository);
    }

    @Test
    @DisplayName("Kastar en Exception när loan by id inte hittas")
    void deleteLoanShouldThrowResourceNotFoundExceptionWhenNotFound() {

        //ARRANGE
        Integer id = 77;

        when(loanRepository.findById(id)).thenReturn(Optional.empty());

        // ACT + ASSERT

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> loanService.deleteLoan(id)
        );

        assertEquals(
                "Loan with id: " + id + "not found",
                ex.getMessage()
        );

        // Verifierar att repository anropas
        verify(loanRepository, times(1)).findById(id);

        // delete ska inte anropas om inget hittas
        verify(loanRepository, never()).delete(any());

        verifyNoMoreInteractions(loanRepository);

    }
}