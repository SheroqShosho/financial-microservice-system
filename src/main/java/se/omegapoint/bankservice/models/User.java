package se.omegapoint.bankservice.models;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
//    private String Country;
//    private String City;
//    private String Address;
//    private String ZipCode;
//    private String phoneNumber;
//    private String yearlyIncome;


    public User(String userId, String firstName, String lastName, String email) {
        userId = userId;
        firstName = firstName;
        lastName = lastName;
        email = email;
    }

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }
}
