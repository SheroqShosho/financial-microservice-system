package se.omegapoint.bankservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.omegapoint.bankservice.dtos.CreditCardResponseDTO;
import se.omegapoint.bankservice.models.CreditCard;

@Mapper(componentModel = "jsr330")
public interface CreditCardMapper {

    @Mapping(target = "userId", expression = "java(creditCard.getRawUserId())")
    @Mapping(target = "availableAmount", expression = "java(creditCard.getCreditLimit().subtract(creditCard.getSpentAmount()))")
    CreditCardResponseDTO toResponseDto(CreditCard creditCard);
}
