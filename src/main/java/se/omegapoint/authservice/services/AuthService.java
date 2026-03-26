package se.omegapoint.authservice.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.omegapoint.authservice.dtos.AuthResponseDTO;
import se.omegapoint.authservice.dtos.GoogleUserInfoDTO;
import se.omegapoint.authservice.models.RefreshToken;
import se.omegapoint.authservice.models.Role;
import se.omegapoint.authservice.models.User;
import se.omegapoint.authservice.repositories.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final GoogleService googleService;

    public AuthService(UserRepository userRepository, JwtService jwtService, RefreshTokenService refreshTokenService, GoogleService googleService ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.googleService = googleService;
    }

    @Transactional
    public AuthResponseDTO loginWithGoogle(String idToken){
        GoogleUserInfoDTO googleUser = googleService.verifyAndExtract(idToken);

        User user = findOrCreateUser(googleUser);

        return buildAuthResponse(user);
    }

    public AuthResponseDTO refreshAccessToken(String refreshToken){
        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);
        return buildAuthResponse(token.getUser());
    }

    @Transactional
    public void logout(String token){

        refreshTokenService.revokeByToken(token);
    }


    private User findOrCreateUser(GoogleUserInfoDTO googleUser){
        return userRepository.findByGoogleId(googleUser.googleId())
                .or(() -> userRepository.findByEmail(googleUser.email()))
                .map(existingUser -> {
                    existingUser.setGoogleId(googleUser.googleId());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    String lastName = (googleUser.lastName() != null) ? googleUser.lastName() : "";

                    User newUser = new User(
                            googleUser.email(),
                            googleUser.firstName(),
                            lastName,
                            Role.USER
                    );
                    newUser.setGoogleId(googleUser.googleId());
                    return userRepository.save(newUser);

                });
    }

    private AuthResponseDTO buildAuthResponse(User user){
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthResponseDTO(accessToken, refreshToken.getToken(), jwtService.getExpiration());
    }


}
