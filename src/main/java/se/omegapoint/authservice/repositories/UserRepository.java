package se.omegapoint.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.omegapoint.authservice.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>{

  Optional<User> findByEmail(String email);

  Optional<User> findByGoogleId(String googleId);

  boolean existsByEmail(String email);
}
