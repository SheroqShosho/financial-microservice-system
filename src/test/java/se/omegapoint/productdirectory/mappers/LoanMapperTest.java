package se.omegapoint.productdirectory.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.models.enums.LoanType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;
import se.omegapoint.productdirectory.models.loans.Loan;
import java.math.BigDecimal;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class LoanMapperTest {

    private final LoanMapper loanMapper = new LoanMapper();


    @Test
    @DisplayName("Ska mappa entitet till response")
    void shouldMapEntityToResponse() {

        // ARRANGE

        // Mocka Loan entity
        Loan entity = mock(Loan.class);

        // Sätt in dummy-värden för entity
        when(entity.getProductId()).thenReturn(7);
        when(entity.getProductType()).thenReturn(ProductType.LOAN);
        when(entity.getProductStatus()).thenReturn(ProductStatus.ACTIVE);
        when(entity.getLoanType()).thenReturn(LoanType.PRIVATE);
        when(entity.getMinAmount()).thenReturn(new BigDecimal("500"));
        when(entity.getMaxAmount()).thenReturn(new BigDecimal("20000"));
        when(entity.getDurationMonths()).thenReturn(6);
        when(entity.getInterestRate()).thenReturn(new BigDecimal("2"));

        // ACT

        // Mappa entity till response
        LoanResponseDTO response = loanMapper.mapToLoanResponse(entity);

        // ASSERT

        // Hur vi förväntar oss att response ska se ut efter mappning
        assertThat(response.productId()).isEqualTo(7);
        assertThat(response.productType()).isEqualTo(ProductType.LOAN);
        assertThat(response.productStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(response.loanType()).isEqualTo(LoanType.PRIVATE);
        assertThat(response.minAmount()).isEqualByComparingTo(new BigDecimal("500"));
        assertThat(response.maxAmount()).isEqualByComparingTo(new BigDecimal("20000"));
        assertThat(response.durationMonths()).isEqualTo(6);
        assertThat(response.interestRate()).isEqualByComparingTo(new BigDecimal("2"));


    }

    @Test
    @DisplayName("Ska mappa request till entitet")
    void shouldMapRequestToEntity() {

        // ARRANGE

        // Skapa en request med dummy data
        LoanRequestDTO request = new LoanRequestDTO(
                ProductStatus.ACTIVE,
                LoanType.PRIVATE,
                new BigDecimal("0"), // minAmount
                new BigDecimal("50000"), // maxAmount
                6,                           // durationMonths
                new BigDecimal("3") // interestRate

        );

        // ACT

        // Mappa om requesten till en entity
        Loan entity = loanMapper.mapToLoan(request);

        // ASSERT

        // Hur vi förväntar oss att datan mappas

        assertThat(entity.getProductStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(entity.getLoanType()).isEqualTo(LoanType.PRIVATE);
        assertThat(entity.getMinAmount()).isEqualByComparingTo(new BigDecimal("0"));
        assertThat(entity.getMaxAmount()).isEqualByComparingTo(new BigDecimal("50000"));
        assertThat(entity.getDurationMonths()).isEqualTo(6);
        assertThat(entity.getInterestRate()).isEqualByComparingTo(new BigDecimal("3"));

        // Kontrollerar att rätt produkttyp blir satt, i detta fall LOAN
        assertThat(entity.getProductType()).isEqualTo(ProductType.LOAN);

        // Kontrollerar att id är null eftersom detta ska genereras automatiskt senare
        assertThat(entity.getProductId()).isNull();



    }

}

