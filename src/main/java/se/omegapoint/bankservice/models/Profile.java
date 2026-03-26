package se.omegapoint.bankservice.models;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@DynamoDbBean
public class Profile {

    private String userId;
    private String firstName;
    private String lastName;
    private String socialSecurityNumber;
    private String country;
    private String city;
    private String address;
    private String zipCode;
    private String phoneNumber;
    private BigDecimal yearlyIncome;
    private String status;

    public Profile() {}

    public Profile(String userId, String firstName, String lastName, String socialSecurityNumber, String country, String city, String address, String zipCode, String phoneNumber, BigDecimal yearlyIncome, String status) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.country = country;
        this.city = city;
        this.address = address;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.yearlyIncome = yearlyIncome;
        this.status = status;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getUserId() {
        return "USER#" + userId;
    }
    public void setUserId(String userId) {
        this.userId = userId.replace("USER#", "");
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getSk() {
        return "PROFILE#" + socialSecurityNumber;
    }

    public void setSk(String sk) {
        // SK är alltid "PROFILE"
    }

    public String getRawUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getYearlyIncome() {
        return yearlyIncome;
    }

    public void setYearlyIncome(BigDecimal yearlyIncome) {
        this.yearlyIncome = yearlyIncome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
