package se.omegapoint.bankservice.repositories;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.models.CreditCard;
import se.omegapoint.bankservice.models.Loan;
import se.omegapoint.bankservice.models.Profile;
import se.omegapoint.bankservice.models.User;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;


@Singleton
public class CustomerRegisterRepository {

    private final DynamoDbAsyncTable<User> userTable;
    private final DynamoDbAsyncTable<Profile> profileTable;
    private final DynamoDbAsyncTable<CreditCard> creditCardTable;
    private final DynamoDbAsyncTable<Loan> loanTable;

    public CustomerRegisterRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient, @Value("${aws.dynamodb.table-name:customerregister}") String tableName) {
        this.userTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(User.class));
        this.profileTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(Profile.class));
        this.creditCardTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(CreditCard.class));
        this.loanTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(Loan.class));
    }

    // CREDITCARD

    public Mono<CreditCard> saveCreditCard(CreditCard creditCard) {
        return Mono.fromFuture(creditCardTable.putItem(creditCard))
                .thenReturn(creditCard);
    }

    public Flux<CreditCard> getAllCreditCardsFromUser(String userId) {
        QueryConditional queryConditional = QueryConditional
                .sortBeginsWith(Key.builder()
                        .partitionValue("USER#" + userId)
                        .sortValue("CARD#")
                        .build());

        return Flux.from(creditCardTable.query(queryConditional).items());
    }

    public Mono<CreditCard> findCreditCardById(String userId, String creditCardType, String creditCardId) {
        QueryConditional queryConditional = QueryConditional
                .sortBeginsWith(Key.builder()
                        .partitionValue("USER#" + userId)
                        .sortValue("CARD#" + creditCardType + "#" + creditCardId)
                        .build());

        return Mono.from(creditCardTable.query(queryConditional).items());
    }

    public Mono<CreditCard> updateCreditCard(CreditCard creditCard) {
        return Mono.fromFuture(creditCardTable.updateItem(creditCard));
    }

    public Mono<Void> deleteCreditCardById(String userId, String creditCardType, String creditCardId) {
        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("CARD#" + creditCardType + "#" + creditCardId)
                .build();
        return Mono.fromFuture(creditCardTable.deleteItem(key))
                .then();
    }


    // LOAN

    public Mono<Loan> saveLoan(Loan loan) {
        return Mono.fromFuture(loanTable.putItem(loan))
                .thenReturn(loan);
    }

    public Flux<Loan> getAllLoansFromUser(String userId) {

        String partitionKey = "USER#" + userId;

        QueryConditional queryConditional = QueryConditional
                .sortBeginsWith(Key.builder()
                        .partitionValue(partitionKey)
                        .sortValue("LOAN#")
                        .build());

        return Flux.from(loanTable.query(queryConditional).items());
    }

    public Mono<Loan> findLoanById(String userId, String loanType, String loanId) {
        QueryConditional queryConditional = QueryConditional
                .sortBeginsWith(Key.builder()
                        .partitionValue("USER#" + userId)
                        .sortValue("LOAN#" + loanType + "#" + loanId)
                        .build());

        return Mono.from(loanTable.query(queryConditional).items());
    }

    public Mono<Loan> updateLoan(Loan loan) {
        return Mono.fromFuture(loanTable.updateItem(loan));
    }

    public Mono<Void> deleteLoanById(String userId, String loanType, String loanId) {
        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("LOAN#" + loanType + "#" + loanId)
                .build();
        return Mono.fromFuture(loanTable.deleteItem(key))
                .then();
    }


    //PROFILE

    public Mono<Profile> saveProfile(Profile profile) {
        return Mono.fromFuture(profileTable.putItem(profile))
                .thenReturn(profile);
    }

    public Flux<Profile> getUserInformation(String userId) {
        QueryConditional queryConditional = QueryConditional
                .sortBeginsWith(Key.builder()
                        .partitionValue("USER#" + userId)
                        .sortValue("PROFILE#")
                        .build());
        return Flux.from(profileTable.query(queryConditional).items());


    }
}
