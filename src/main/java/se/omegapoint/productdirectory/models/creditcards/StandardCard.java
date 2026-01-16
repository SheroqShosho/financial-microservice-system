package se.omegapoint.productdirectory.models.creditcards;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import se.omegapoint.productdirectory.models.Product;
import se.omegapoint.productdirectory.models.enums.CreditCardType;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

import javax.smartcardio.Card;
import java.math.BigDecimal;

@Entity
public class StandardCard extends CreditCard {

    protected StandardCard() {
    }

    protected StandardCard(CreditCardType creditCardType,
                           BigDecimal spentAmount,
                           BigDecimal creditLimit,
                           BigDecimal fee,
                           BigDecimal interestRate,
                           ProductType productType,
                           ProductStatus productStatus) {

        super(creditCardType, spentAmount, creditLimit, fee, interestRate, productType, productStatus);
    }


}
