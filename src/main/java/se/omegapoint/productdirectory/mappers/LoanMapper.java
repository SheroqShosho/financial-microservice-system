package se.omegapoint.productdirectory.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.dtos.LoanUpdateDTO;
import se.omegapoint.productdirectory.models.enums.ProductType;
import se.omegapoint.productdirectory.models.loans.Loan;

@Component
public class LoanMapper {

    private static final Logger log = LoggerFactory.getLogger(LoanMapper.class);

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
        entity.setProductType(ProductType.LOAN);
        entity.setProductStatus(request.productStatus());
        entity.setLoanType(request.loanType());
        entity.setMinAmount(request.minAmount());
        entity.setMaxAmount(request.maxAmount());
        entity.setDurationMonths(request.durationMonths());
        entity.setInterestRate(request.interestRate());
        return entity;
    }

    // Mapper för att uppdatera befintlig entity i databasen - behåller befintligt värde om null skickas in
    public void updateEntityFromDTO(LoanUpdateDTO request, Loan entity) {

        if (request.productStatus() != null) {
            log.info("   Field productStatus updated to : {}", request.productStatus());
            entity.setProductStatus(request.productStatus());
        }
        if (request.loanType() != null) {
            log.info("   Field loanType updated to : {}", request.loanType());
            entity.setLoanType(request.loanType());
        }
        if (request.minAmount() != null) {
            log.info("   Field minAmount updated to : {}", request.minAmount());
            entity.setMinAmount(request.minAmount());
        }
        if (request.maxAmount() != null) {
            log.info("   Field maxAmount updated to : {}", request.maxAmount());
            entity.setMaxAmount(request.maxAmount());
        }
        if (request.durationMonths() != null) {
            log.info("   Field durationMonths updated to : {}", request.durationMonths());
            entity.setDurationMonths(request.durationMonths());
        }
        if (request.interestRate() != null) {
            log.info("   Field interestRate updated to : {}", request.interestRate());
            entity.setInterestRate(request.interestRate());
        }
    }
}
