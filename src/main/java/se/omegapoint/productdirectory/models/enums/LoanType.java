package se.omegapoint.productdirectory.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LoanType {
    PRIVATE,
    MORTGAGE;

    // Gör om all input till upper case
    @JsonCreator
    public static LoanType fromString(String value) {

        if (value == null) return null;

        try {
            return LoanType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
