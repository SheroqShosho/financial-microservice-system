package se.omegapoint.productdirectory.mappers;

import org.springframework.stereotype.Component;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.models.creditcards.CreditCard;

@Component
public class CreditCardMapper {

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
        entity.setProductType(request.productType());
        entity.setProductStatus(request.productStatus());
        entity.setCreditCardType(request.creditCardType());
        entity.setSpentAmount(request.spentAmount());
        entity.setCreditLimit(request.creditLimit());
        entity.setFee(request.fee());
        entity.setInterestRate(request.interestRate());
        return entity;
    }
}
