package se.omegapoint.bankservice.repositories;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.models.CreditCard;
import se.omegapoint.bankservice.models.Loan;
import se.omegapoint.bankservice.models.Profile;
import se.omegapoint.bankservice.models.User;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;


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

    public Mono<CreditCard> saveCreditCard(CreditCard creditCard) {
        return Mono.fromFuture(creditCardTable.putItem(creditCard))
                .thenReturn(creditCard);
    }
}
