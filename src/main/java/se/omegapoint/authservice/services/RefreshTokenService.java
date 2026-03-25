package se.omegapoint.authservice.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.omegapoint.authservice.models.RefreshToken;
import se.omegapoint.authservice.models.User;
import se.omegapoint.authservice.repositories.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${JWT_REFRESH_EXPIRATION}")
    private long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // Skapar en ny refresh token för användaren och ogiltigförklarar tidigare tokens.
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        logger.debug("Creating refresh token for userId={}", user.getUserId());

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(java.time.Instant.now().plusMillis(refreshTokenExpiration));

        logger.info("Refresh token created for userId={}", user.getUserId());
        return refreshTokenRepository.save(refreshToken);
    }

    // Validerar att refresh token finns och inte har gått ut.
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            logger.warn("Expired refresh token removed for userId={}", refreshToken.getUser().getUserId());
            throw new RuntimeException("Refresh token expired");
        }

        logger.debug("Refresh token validated for userId={}", refreshToken.getUser().getUserId());
        return refreshToken;
    }

    // Återkallar en refresh token så att den inte längre kan användas.
    @Transactional
    public void revokeByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token hittades inte"));

        refreshTokenRepository.delete(refreshToken);
        logger.info("Refresh token revoked for userId={}", refreshToken.getUser().getUserId());
    }


}
