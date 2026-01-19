package se.omegapoint.productdirectory.mapper;

import org.springframework.stereotype.Component;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.models.loans.Loan;
@Component
public class LoanMapper {

    public LoanResponseDTO mapToLoanResponse(Loan loan) {
        return new LoanResponseDTO(
                loan.getProductId(),
                loan.getProductType(),
                loan.getProductStatus(),
                loan.getLoanType(),
                loan.getMinAmount(),
                loan.getMaxAmount(),
                loan.getDurationMonths(),
                loan.getInterestRate()
        );
    }

    public Loan mapToLoan(LoanRequestDTO loanRequestDTO) {
        Loan loan = new Loan();
        loan.setProductType(loanRequestDTO.productType());
        loan.setProductStatus(loanRequestDTO.productStatus());
        loan.setLoanType(loanRequestDTO.loanType());
        loan.setMinAmount(loanRequestDTO.minAmount());
        loan.setMaxAmount(loanRequestDTO.maxAmount());
        loan.setDurationMonths(loanRequestDTO.durationMonths());
        loan.setInterestRate(loanRequestDTO.interestRate());
        return loan;
    }
}
