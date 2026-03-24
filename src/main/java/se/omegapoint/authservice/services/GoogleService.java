package se.omegapoint.authservice.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.omegapoint.authservice.dtos.GoogleUserInfoDTO;

import java.util.Collections;

@Service
public class GoogleService {

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    private void init() {
        verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

    }

    public GoogleUserInfoDTO verifyAndExtract(String idToken) {
        try {

        GoogleIdToken token = verifier.verify(idToken);
        if (token == null) {
            throw new RuntimeException("Invalid ID token");
        }

        GoogleIdToken.Payload payload = token.getPayload();

        return new GoogleUserInfoDTO(
                payload.getSubject(),
                payload.getEmail(),
                (String) payload.get("given_name"),
                (String) payload.getOrDefault("family_name", "")
        );
    } catch (Exception e) {
            throw new RuntimeException("Failed to verify ID token", e);
        }
    }
}
