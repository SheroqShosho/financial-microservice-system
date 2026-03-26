package se.omegapoint.authservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.omegapoint.authservice.models.User;


import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${JWT_SECRET}")
    private String secret;

    @Value("${JWT_EXPIRATION}")
    private long expiration;

    private SecretKey secretKey;

    public long getExpiration() {
        return expiration;
    }

    // Bygger HMAC-nyckeln när konfigurationsvärdena har injicerats.
    @PostConstruct
    private void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        logger.info("JWT service initialized");
    }

    // Genererar en signerad access token för en användare.
    public String generateToken(User user){
        logger.debug("Generating JWT for userId={}", user.getUserId());
        return Jwts.builder()
                .subject(String.valueOf(user.getUserId()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    // Validerar tokens signatur och utgångstid, returnerar true om giltig.
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            logger.debug("JWT validation succeeded");
            return true;
        } catch (Exception e) {
            logger.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    // Parsar token och returnerar claims-payload.
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Hämtar användar-ID från tokenens subject.
    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    // Hämtar e-post-claim från token.
    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    // Hämtar roll-claim från token.
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
