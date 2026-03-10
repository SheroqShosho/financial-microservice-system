package se.omegapoint.bankservice.clients;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Factory // Skapar instans av client
public class DynamoDBFactory {

    @Singleton
    public DynamoDbClient ddbClient() {
        return DynamoDbClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .build();
    }

    @Singleton
    public DynamoDbAsyncClient ddbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .build();
    }

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
