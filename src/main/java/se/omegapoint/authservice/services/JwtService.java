package se.omegapoint.authservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import se.omegapoint.authservice.models.User;


import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey secretKey;

    // Körs efter att @Value-fälten injicerats, bygger secretKey från application.properties
    @PostConstruct
    private void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Genererar ett JWT access token för en användare
    public String generateTokenToken(User user){
        return Jwts.builder()
                .subject(String.valueOf(user.getUserId()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    // Returnerar true om tokenet är giltigt, annars false
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Parsar tokenet och returnerar all data som finns i payload
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Hämtar userId ur tokenet
    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    // Hämtar email ur tokenet
    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    // Hämtar sole ur tokenet
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
