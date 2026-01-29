package se.omegapoint.productdirectory.models.enums;

public enum ProductType {
    CREDIT_CARD,
    LOAN;


//    // Gör om all input till rätt format
//    @JsonCreator
//    public static ProductType fromString(String value) {
//       if (value == null) return null;
//
//       try {
//           String val = value.replaceAll("[ -]", "").toUpperCase();
//           if (val.equals("CREDITCARD"))
//               return CREDIT_CARD;
//
//           return ProductType.valueOf(val);
//       } catch (IllegalArgumentException e) {
//           return null;
//       }
//    }

}
