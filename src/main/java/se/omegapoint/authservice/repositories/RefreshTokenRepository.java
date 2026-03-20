package se.omegapoint.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.omegapoint.authservice.models.RefreshToken;
import se.omegapoint.authservice.models.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}
