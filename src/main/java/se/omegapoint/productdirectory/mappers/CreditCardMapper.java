package se.omegapoint.productdirectory.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.models.creditcards.CreditCard;
import se.omegapoint.productdirectory.models.enums.ProductType;

@Component
public class CreditCardMapper {

    private static final Logger log = LoggerFactory.getLogger(CreditCardMapper.class);

    // Mapper för response med ID (Entity > DTO)
    public CreditCardResponseDTO mapToCreditCardResponse(CreditCard entity) {
        return new CreditCardResponseDTO(
                entity.getProductId(),
                entity.getProductType(),
                entity.getProductStatus(),
                entity.getCreditCardType(),
                entity.getSpentAmount(),
                entity.getCreditLimit(),
                entity.getFee(),
                entity.getInterestRate()
        );
    }

    // Mapper för request utan ID (DTO > Entity)
    public CreditCard mapToCreditCardEntity(CreditCardRequestDTO request) {
        CreditCard entity = new CreditCard();
        entity.setProductType(ProductType.CREDIT_CARD);
        entity.setProductStatus(request.productStatus());
        entity.setCreditCardType(request.creditCardType());
        entity.setSpentAmount(request.spentAmount());
        entity.setCreditLimit(request.creditLimit());
        entity.setFee(request.fee());
        entity.setInterestRate(request.interestRate());
        return entity;
    }

    // Mapper för att uppdatera befintlig entity i databasen - behåller befintligt värde om null skickas in
    public void updateEntityFromDTO(CreditCardRequestDTO request, CreditCard entity) {
//        if (request.productType() != null) {
//            log.info("   Field productType updated to : {}", request.productType());
//            entity.setProductType(request.productType());
//        }
        if (request.productStatus() != null) {
            log.info("   Field productStatus updated to : {}", request.productStatus());
            entity.setProductStatus(request.productStatus());
        }
        if (request.creditCardType() != null) {
            log.info("   Field creditCardType updated to : {}", request.creditCardType());
            entity.setCreditCardType(request.creditCardType());
        }
        if (request.spentAmount() != null) {
            log.info("   Field spentAmount updated to : {}", request.spentAmount());
            entity.setSpentAmount(request.spentAmount());
        }
        if (request.creditLimit() != null) {
            log.info("   Field creditLimit updated to : {}", request.creditLimit());
            entity.setCreditLimit(request.creditLimit());
        }
        if (request.fee() != null) {
            log.info("   Field fee updated to : {}", request.fee());
            entity.setFee(request.fee());
        }
        if (request.interestRate() != null) {
            log.info("   Field interestRate updated to : {}", request.interestRate());
            entity.setInterestRate(request.interestRate());
        }
    }
}
