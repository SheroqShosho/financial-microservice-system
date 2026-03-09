package se.omegapoint.productdirectory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.omegapoint.productdirectory.models.creditcards.CreditCard;

import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    Optional<CreditCard> findByCreditCardType(String creditCardType);
}
