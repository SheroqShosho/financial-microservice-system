package se.omegapoint.bankservice.repositories;

import jakarta.inject.Singleton;
import se.omegapoint.bankservice.models.CreditCard;
import se.omegapoint.bankservice.models.Loan;
import se.omegapoint.bankservice.models.User;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;

@Singleton
public class CustomerRegisterRepository {

    private final DynamoDbTable<User> userTable;

    public CustomerRegisterRepository(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("CustomerRegister",
                TableSchema.fromBean(User.class));
    }

    public void save(User user) {
        userTable.putItem(user);
    }

    public User findById(String userId) {
        Key key = Key.builder()
                .partitionValue(userId)
                .build();

        return userTable.getItem(key);
    }

    public void delete(String userId) {
        userTable.deleteItem(Key.builder()
                .partitionValue(userId)
                .build());
    }

    public List<User> findAll() {
        return userTable.scan()
                .items()
                .stream()
                .toList();
    }

    public List<CreditCard> findCardsByUserId(String userId) {
        User user = findById(userId);
        return (user != null && user.getCreditCards() != null)
                ? user.getCreditCards()
                : List.of();
    }

    public List<Loan> findLoansByUserId(String userId) {
        User user = findById(userId);
        return (user != null && user.getLoans() != null)
                ? user.getLoans()
                : List.of();
    }
}
