package se.omegapoint.bankservice.services;


import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.clients.Pd1Client;
import se.omegapoint.bankservice.dtos.*;

@Singleton
public class TemplateService {

    private final Pd1Client pd1Client;


    private static final Logger LOG = LoggerFactory.getLogger(TemplateService.class);

    public TemplateService(Pd1Client pd1Client) {
        this.pd1Client = pd1Client;
    }

    // CREDITCARD

    public Flux<CreditCardTemplateResponseDTO> getAll() {
        LOG.info("Fetching all credit card templates");

        return pd1Client.getAllCreditCardTemplates()
                .doOnNext(template -> LOG.debug("Found credit card template: {}", template))
                .doOnComplete(() -> LOG.debug("Completed fetching credit card templates"))
                .doOnError(e -> LOG.error("Error fetching credit card templates", e));
    }

    public Mono<CreditCardTemplateResponseDTO> createCreditCardTemplate(CreditCardTemplateRequestDTO request) {
        LOG.info("Creating credit card template");

        return pd1Client.createCreditCardTemplate(request)
                .doOnSuccess(response -> LOG.debug("Created credit card template: {}", response))
                .doOnError(e -> LOG.error("Error creating credit card template", e));
    }

    public Mono<CreditCardTemplateResponseDTO> updateCreditCardTemplate(String id, CreditCardTemplateUpdateDTO request) {
        LOG.info("Updating credit card template id={}", id);

        return pd1Client.updateCreditCardTemplate(id, request)
                .doOnSuccess(response -> LOG.debug("Updated credit card template id={}, result={}", id, response))
                .doOnError(e -> LOG.error("Error updating credit card template id={}", id, e));
    }

    public Mono<Void> deleteCreditCardTemplate(String id) {
        LOG.info("Deleting credit card template id={}", id);

        return pd1Client.deleteCreditCardTemplate(id)
                .doOnSuccess(unused -> LOG.debug("Deleted credit card template id={}", id))
                .doOnError(e -> LOG.error("Error deleting credit card template id={}", id, e));
    }

    // LOAN

    public Flux<LoanTemplateResponseDTO> getAllLoanTemplates() {
        LOG.info("Fetching all loan templates");

        return pd1Client.getAllLoanTemplates()
                .doOnNext(template -> LOG.debug("Found loan template: {}", template))
                .doOnComplete(() -> LOG.debug("Completed fetching loan templates"))
                .doOnError(e -> LOG.error("Error fetching loan templates", e));
    }

    public Mono<LoanTemplateResponseDTO> createLoanTemplate(LoanTemplateRequestDTO request) {
        LOG.info("Creating loan template");

        return pd1Client.createLoanTemplate(request)
                .doOnSuccess(response -> LOG.debug("Created loan template: {}", response))
                .doOnError(e -> LOG.error("Error creating loan template", e));
    }

    public Mono<LoanTemplateResponseDTO> updateLoanTemplate(String id, LoanTemplateUpdateDTO request) {
        LOG.info("Updating loan template id={}", id);

        return pd1Client.updateLoanTemplate(id, request)
                .doOnSuccess(response -> LOG.debug("Updated loan template id={}, result={}", id, response))
                .doOnError(e -> LOG.error("Error updating loan template id={}", id, e));
    }

    public Mono<Void> deleteLoanTemplate(String id) {
        LOG.info("Deleting loan template id={}", id);

        return pd1Client.deleteLoanTemplate(id)
                .doOnSuccess(unused -> LOG.debug("Deleted loan template id={}", id))
                .doOnError(e -> LOG.error("Error deleting loan template id={}", id, e));
    }
}

