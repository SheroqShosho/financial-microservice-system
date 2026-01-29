package se.omegapoint.productdirectory.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CreditCardType {
    STANDARD,
    GOLD,
    PLATINUM;

    // Gör om all input till upper case
    @JsonCreator
    public static CreditCardType fromString(String value) {
        if (value == null) return null;

        try {
            return CreditCardType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
