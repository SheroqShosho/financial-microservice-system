package se.omegapoint.bankservice.clients;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Factory // Skapar instans av client
public class DynamoDBFactory {

    @Singleton
    public DynamoDbEnhancedClient enhancedClient(DynamoDbClient ddbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddbClient)
                .build();
    }

    @Singleton
    public DynamoDbEnhancedAsyncClient enhancedAsyncClient(DynamoDbAsyncClient ddbAsyncClient) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(ddbAsyncClient)
                .build();
    }
}
