package se.omegapoint.productdirectory.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductStatus {
    ACTIVE, // Produkt är aktiverad
    PENDING, // Produkt är skapad men inte aktiverad
    SUSPENDED, // Produkt är spärrad
    RETIRED; // Produkt är avslutad

    // Gör om all input till upper case
    @JsonCreator
    public static ProductStatus fromString(String value) {
        if (value == null) return null;

        try {
            return ProductStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
