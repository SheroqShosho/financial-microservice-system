package se.omegapoint.authservice.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.omegapoint.authservice.dtos.AuthResponseDTO;
import se.omegapoint.authservice.dtos.GoogleTokenRequestDTO;
import se.omegapoint.authservice.dtos.RefreshTokenRequestDTO;
import se.omegapoint.authservice.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Tar emot Google ID-token och returnerar access/refresh token.
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginWithGoogle(@Valid @RequestBody GoogleTokenRequestDTO request) {
        log.info("Login request received");

        AuthResponseDTO response = authService.loginWithGoogle(request.idToken());
        return ResponseEntity.ok(response);
    }

    // Tar emot refresh token och returnerar ett nytt token-par.
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        AuthResponseDTO response = authService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    // Tar emot refresh token och loggar ut användaren genom att återkalla token.
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDTO request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();

    }


}
