package se.omegapoint.authservice.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.omegapoint.authservice.dtos.GoogleUserInfoDTO;

import java.util.Collections;

@Service
public class GoogleService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleService.class);

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    private GoogleIdTokenVerifier verifier;

    // Initierar verifierare för Google ID-token efter att konfigurationen har injicerats.
    @PostConstruct
    private void init() {
        verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        logger.info("Google token verifier initialized");

    }

    // Verifierar en Google ID-token och mappar payload till vår DTO.
    public GoogleUserInfoDTO verifyAndExtract(String idToken) {
        try {
        logger.debug("Google ID token verification started");

        GoogleIdToken token = verifier.verify(idToken);
        if (token == null) {
            logger.warn("Google ID token verification failed: token is invalid");
            throw new RuntimeException("Invalid ID token");
        }

        GoogleIdToken.Payload payload = token.getPayload();
        logger.info("Google ID token verified for email={}", payload.getEmail());

        return new GoogleUserInfoDTO(
                payload.getSubject(),
                payload.getEmail(),
                (String) payload.get("given_name"),
                (String) payload.getOrDefault("family_name", "")
        );
    } catch (Exception e) {
            logger.error("Failed to verify Google ID token", e);
            throw new RuntimeException("Failed to verify ID token", e);
        }
    }
}
