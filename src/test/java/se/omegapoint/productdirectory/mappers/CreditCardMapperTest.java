package se.omegapoint.productdirectory.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.models.creditcards.CreditCard;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreditCardMapperTest {


    private final CreditCardMapper creditCardMapper = new CreditCardMapper();


    @Test
    @DisplayName("Ska mappa om entitet till response")
    void shouldMapEntityToResponse() {

        // ARRANGE

        // Mocka CreditCard entity
        CreditCard entity = mock(CreditCard.class);

        // Sätt in dummy-värden för entity
        when(entity.getProductId()).thenReturn(7);
        when(entity.getProductType()).thenReturn(ProductType.CREDIT_CARD);
        when(entity.getProductStatus()).thenReturn(ProductStatus.ACTIVE);
        when(entity.getCreditCardType()).thenReturn("GOLD");
        when(entity.getCreditLimit()).thenReturn(new BigDecimal("20000"));
        when(entity.getFee()).thenReturn(new BigDecimal("200"));
        when(entity.getInterestRate()).thenReturn(new BigDecimal("2"));

        // ACT

        // Mappa enity till response
        CreditCardResponseDTO response = creditCardMapper.mapToCreditCardResponse(entity);

        // ASSERT

        // Hur vi förväntar oss att responsen ska se ut efter mappning
        assertThat(response.productId()).isEqualTo(7);
        assertThat(response.productType()).isEqualTo(ProductType.CREDIT_CARD);
        assertThat(response.productStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(response.creditCardType()).isEqualTo("GOLD");
        assertThat(response.creditLimit()).isEqualByComparingTo(new BigDecimal("20000"));
        assertThat(response.fee()).isEqualByComparingTo(new BigDecimal("200"));
        assertThat(response.interestRate()).isEqualByComparingTo(new BigDecimal("2"));


    }

    @Test
    @DisplayName("Ska mappa request till entitet")
    void shouldMapRequestToEntity() {

        // ARRANGE

        // Skapa en request med dummy data
        CreditCardRequestDTO request = new CreditCardRequestDTO(
                ProductStatus.ACTIVE,
                ("GOLD"),
                new BigDecimal("50000"), // creditLimit
                new BigDecimal("150"), // fee
                new BigDecimal("3") // interestRate

        );

        // ACT

        // Mappa om requesten till en entity
        CreditCard entity = creditCardMapper.mapToCreditCardEntity(request);

        // ASSERT

        // Hur vi förväntar oss att datan mappas

        assertThat(entity.getProductStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(entity.getCreditCardType()).isEqualTo("GOLD");
        assertThat(entity.getCreditLimit()).isEqualByComparingTo(new BigDecimal("50000"));
        assertThat(entity.getFee()).isEqualByComparingTo(new BigDecimal("150"));
        assertThat(entity.getInterestRate()).isEqualByComparingTo(new BigDecimal("3"));

        // Kontrollerar att rätt produkttyp blir satt, i detta fall CREDIT_CARD
        assertThat(entity.getProductType()).isEqualTo(ProductType.CREDIT_CARD);

        // Kontrollerar att id är null eftersom detta ska genereras automatiskt senare
        assertThat(entity.getProductId()).isNull();


    }

}
