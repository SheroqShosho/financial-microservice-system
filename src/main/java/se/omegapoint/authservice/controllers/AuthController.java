package se.omegapoint.authservice.controllers;

import jakarta.validation.Valid;
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

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginWithGoogle(@Valid @RequestBody GoogleTokenRequestDTO request) {

        AuthResponseDTO response = authService.loginWithGoogle(request.idToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        AuthResponseDTO response = authService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDTO request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();

    }


}
