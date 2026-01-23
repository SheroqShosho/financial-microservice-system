package se.omegapoint.productdirectory.mappers;

import org.springframework.stereotype.Component;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.models.loans.Loan;

@Component
public class LoanMapper {

    // Mapper för response med ID (Entity > DTO)
    public LoanResponseDTO mapToLoanResponse(Loan entity) {
        return new LoanResponseDTO(
                entity.getProductId(),
                entity.getProductType(),
                entity.getProductStatus(),
                entity.getLoanType(),
                entity.getMinAmount(),
                entity.getMaxAmount(),
                entity.getDurationMonths(),
                entity.getInterestRate()
        );
    }

    // Mapper för request utan ID (DTO > Entity)
    public Loan mapToLoan(LoanRequestDTO request) {
        Loan entity = new Loan();
        entity.setProductType(request.productType());
        entity.setProductStatus(request.productStatus());
        entity.setLoanType(request.loanType());
        entity.setMinAmount(request.minAmount());
        entity.setMaxAmount(request.maxAmount());
        entity.setDurationMonths(request.durationMonths());
        entity.setInterestRate(request.interestRate());
        return entity;
    }

    public void updateEntityFromDTO(LoanRequestDTO request, Loan entity) {
        if (request.productType() != null) {
            entity.setProductType(request.productType());
        }
        if (request.productStatus() != null) {
            entity.setProductStatus(request.productStatus());
        }
        if (request.loanType() != null) {
            entity.setLoanType(request.loanType());
        }
        if (request.minAmount() != null) {
            entity.setMinAmount(request.minAmount());
        }
        if (request.maxAmount() != null) {
            entity.setMaxAmount(request.maxAmount());
        }
        if (request.durationMonths() != null) {
            entity.setDurationMonths(request.durationMonths());
        }
        if (request.interestRate() != null) {
            entity.setInterestRate(request.interestRate());
        }
    }
}
