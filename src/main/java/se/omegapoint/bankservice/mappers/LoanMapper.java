package se.omegapoint.bankservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.omegapoint.bankservice.dtos.LoanResponseDTO;
import se.omegapoint.bankservice.models.Loan;

@Mapper(componentModel = "jsr330")
public interface LoanMapper {

    @Mapping(target = "userId", expression = "java(loan.getRawUserId())")
    LoanResponseDTO toResponseDTO(Loan loan);

}
