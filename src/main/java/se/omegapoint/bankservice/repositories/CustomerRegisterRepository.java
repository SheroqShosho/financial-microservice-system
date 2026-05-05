package se.omegapoint.bankservice.repositories;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.models.CreditCard;
import se.omegapoint.bankservice.models.Loan;
import se.omegapoint.bankservice.models.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Singleton
public class CustomerRegisterRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerRegisterRepository.class);

    private final DynamoDbAsyncTable<Profile> profileTable;
    private final DynamoDbAsyncTable<CreditCard> creditCardTable;
    private final DynamoDbAsyncTable<Loan> loanTable;

    public CustomerRegisterRepository(
            DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
            @Value("${aws.dynamodb.table-name}") String tableName
    ) {
        this.profileTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(Profile.class));
        this.creditCardTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(CreditCard.class));
        this.loanTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(Loan.class));
    }

    // CREDITCARD

    public Mono<CreditCard> saveCreditCard(CreditCard creditCard) {
        LOG.info("Saving credit card for userId={}", creditCard.getUserId());

        return Mono.fromFuture(creditCardTable.putItem(creditCard))
                .doOnSuccess(unused -> LOG.debug("Saved credit card: {}", creditCard))
                .doOnError(e -> LOG.error("Error saving credit card for userId={}", creditCard.getUserId(), e))
                .thenReturn(creditCard);
    }

    public Flux<CreditCard> getAllCreditCardsFromUser(String userId) {
        LOG.info("Fetching all credit cards for userId={}", userId);

        QueryConditional queryConditional = QueryConditional
                .sortBeginsWith(Key.builder()
                        .partitionValue("USER#" + userId)
                        .sortValue("CARD#")
                        .build());

        return Flux.from(creditCardTable.query(queryConditional).items())
                .doOnNext(card -> LOG.debug("Found credit card: {}", card))
                .doOnComplete(() -> LOG.debug("Completed fetching credit cards for userId={}", userId))
                .doOnError(e -> LOG.error("Error fetching credit cards for userId={}", userId, e));
    }

    public Mono<CreditCard> findCreditCardById(String userId, String creditCardType, String creditCardId) {
        LOG.info("Fetching credit card userId={}, type={}, id={}", userId, creditCardType, creditCardId);

        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("CARD#" + creditCardType + "#" + creditCardId)
                .build();

        return Mono.fromFuture(creditCardTable.getItem(key))
                .doOnNext(card -> LOG.debug("Found credit card: {}", card))
                .doOnSuccess(card -> {
                    if (card == null) {
                        LOG.debug("No credit card found for userId={}, type={}, id={}", userId, creditCardType, creditCardId);
                    }
                })
                .doOnError(e -> LOG.error("Error fetching credit card userId={}, type={}, id={}", userId, creditCardType, creditCardId, e));
    }

    public Mono<CreditCard> updateCreditCard(CreditCard creditCard) {
        LOG.info("Updating credit card for userId={}", creditCard.getUserId());

        return Mono.fromFuture(creditCardTable.updateItem(creditCard))
                .doOnSuccess(updatedCard -> LOG.debug("Updated credit card: {}", updatedCard))
                .doOnError(e -> LOG.error("Error updating credit card for userId={}", creditCard.getUserId(), e));
    }

    public Mono<Void> deleteCreditCardById(String userId, String creditCardType, String creditCardId) {
        LOG.info("Deleting credit card userId={}, type={}, id={}", userId, creditCardType, creditCardId);

        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("CARD#" + creditCardType + "#" + creditCardId)
                .build();

        return Mono.fromFuture(creditCardTable.deleteItem(key))
                .doOnSuccess(deletedCard -> LOG.debug("Deleted credit card: {}", deletedCard))
                .doOnError(e -> LOG.error("Error deleting credit card userId={}, type={}, id={}", userId, creditCardType, creditCardId, e))
                .then();
    }

    // LOAN

    public Mono<Loan> saveLoan(Loan loan) {
        LOG.info("Saving loan for userId={}", loan.getUserId());

        return Mono.fromFuture(loanTable.putItem(loan))
                .doOnSuccess(unused -> LOG.debug("Saved loan: {}", loan))
                .doOnError(e -> LOG.error("Error saving loan for userId={}", loan.getUserId(), e))
                .thenReturn(loan);
    }

    public Flux<Loan> getAllLoansFromUser(String userId) {
        LOG.info("Fetching all loans for userId={}", userId);

        QueryConditional queryConditional = QueryConditional
                .sortBeginsWith(Key.builder()
                        .partitionValue("USER#" + userId)
                        .sortValue("LOAN#")
                        .build());

        return Flux.from(loanTable.query(queryConditional).items())
                .doOnNext(loan -> LOG.debug("Found loan: {}", loan))
                .doOnComplete(() -> LOG.debug("Completed fetching loans for userId={}", userId))
                .doOnError(e -> LOG.error("Error fetching loans for userId={}", userId, e));
    }

    public Mono<Loan> findLoanById(String userId, String loanType, String loanId) {
        LOG.info("Fetching loan userId={}, type={}, id={}", userId, loanType, loanId);

        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("LOAN#" + loanType + "#" + loanId)
                .build();

        return Mono.fromFuture(loanTable.getItem(key))
                .doOnNext(loan -> LOG.debug("Found loan: {}", loan))
                .doOnSuccess(loan -> {
                    if (loan == null) {
                        LOG.debug("No loan found for userId={}, type={}, id={}", userId, loanType, loanId);
                    }
                })
                .doOnError(e -> LOG.error("Error fetching loan userId={}, type={}, id={}", userId, loanType, loanId, e));
    }

    public Mono<Loan> updateLoan(Loan loan) {
        LOG.info("Updating loan for userId={}", loan.getUserId());

        return Mono.fromFuture(loanTable.updateItem(loan))
                .doOnSuccess(updatedLoan -> LOG.debug("Updated loan: {}", updatedLoan))
                .doOnError(e -> LOG.error("Error updating loan for userId={}", loan.getUserId(), e));
    }

    public Mono<Void> deleteLoanById(String userId, String loanType, String loanId) {
        LOG.info("Deleting loan userId={}, type={}, id={}", userId, loanType, loanId);

        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("LOAN#" + loanType + "#" + loanId)
                .build();

        return Mono.fromFuture(loanTable.deleteItem(key))
                .doOnSuccess(deletedLoan -> LOG.debug("Deleted loan: {}", deletedLoan))
                .doOnError(e -> LOG.error("Error deleting loan userId={}, type={}, id={}", userId, loanType, loanId, e))
                .then();
    }

    // PROFILE

    public Mono<Profile> saveProfile(Profile profile) {
        LOG.info("Saving profile for userId={}", profile.getUserId());

        return Mono.fromFuture(profileTable.putItem(profile))
                .doOnSuccess(unused -> LOG.debug("Saved profile: {}", profile))
                .doOnError(e -> LOG.error("Error saving profile for userId={}", profile.getUserId(), e))
                .thenReturn(profile);
    }

    public Mono<Profile> getUserInformation(String userId) {
        LOG.info("Fetching profile information for userId={}", userId);

        // Use findProfileByUserId instead to ensure consistency with exact "PROFILE" SK
        return findProfileByUserId(userId);
    }

    public Mono<Profile> findProfileByUserId(String userId) {
        LOG.info("Fetching profile for userId={}", userId);

        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("PROFILE")
                .build();

        return Mono.fromFuture(profileTable.getItem(key))
                .doOnNext(profile -> LOG.debug("Found profile: {}", profile))
                .doOnSuccess(profile -> {
                    if (profile == null) {
                        LOG.debug("No profile found for userId={}", userId);
                    }
                })
                .doOnError(e -> LOG.error("Error fetching profile for userId={}", userId, e));
    }

    public Mono<Profile> updateProfile(Profile profile) {
        LOG.info("Updating profile for userId={}", profile.getUserId());

        return Mono.fromFuture(profileTable.updateItem(profile))
                .doOnSuccess(updatedProfile -> LOG.debug("Updated profile: {}", updatedProfile))
                .doOnError(e -> LOG.error("Error updating profile for userId={}", profile.getUserId(), e));
    }

    public Mono<Void> deleteProfileById(String userId) {
        LOG.info("Deleting profile for userId={}", userId);

        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("PROFILE")
                .build();

        return Mono.fromFuture(profileTable.deleteItem(key))
                .doOnSuccess(deletedProfile -> LOG.debug("Deleted profile: {}", deletedProfile))
                .doOnError(e -> LOG.error("Error deleting profile for userId={}", userId, e))
                .then();
    }
}