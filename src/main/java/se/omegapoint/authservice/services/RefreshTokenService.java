package se.omegapoint.authservice.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.omegapoint.authservice.models.RefreshToken;
import se.omegapoint.authservice.models.User;
import se.omegapoint.authservice.repositories.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${JWT_REFRESH_EXPIRATION}")
    private long refreshtokenexpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // skapar refreshtoken
    @Transactional
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(java.time.Instant.now().plusMillis(refreshtokenexpiration));

        return refreshTokenRepository.save(refreshToken);
    }

    //Validerar refreshtoken, kollar om den finns och inte har gått ut
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token hittades inte"));

        refreshTokenRepository.delete(refreshToken);
    }


}
