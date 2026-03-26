package se.omegapoint.authservice.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.omegapoint.authservice.dtos.AuthResponseDTO;
import se.omegapoint.authservice.dtos.GoogleUserInfoDTO;
import se.omegapoint.authservice.models.RefreshToken;
import se.omegapoint.authservice.models.Role;
import se.omegapoint.authservice.models.User;
import se.omegapoint.authservice.repositories.UserRepository;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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

    // Loggar in användare via Google och skapar en ny session.
    @Transactional
    public AuthResponseDTO loginWithGoogle(String idToken){
        logger.info("Google login started");
        GoogleUserInfoDTO googleUser = googleService.verifyAndExtract(idToken);

        User user = findOrCreateUser(googleUser);

        logger.info("Google login completed for userId={}", user.getUserId());

        return createNewSessionResponse(user);
    }

    // Förnyar access token utifrån en giltig refresh token.
    public AuthResponseDTO refreshAccessToken(String refreshToken){
        logger.info("Access token refresh started");
        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);
        logger.info("Access token refresh completed for userId={}", token.getUser().getUserId());
        return createRefreshResponse(token.getUser(), token);
    }

    // Loggar ut användaren genom att återkalla refresh token.
    @Transactional
    public void logout(String token){
        logger.info("Logout started");
        refreshTokenService.revokeByToken(token);
        logger.info("Logout completed");
    }

    // Hämtar en befintlig användare eller skapar en ny om den saknas.
    private User findOrCreateUser(GoogleUserInfoDTO googleUser){
        return userRepository.findByGoogleId(googleUser.googleId())
                .or(() -> userRepository.findByEmail(googleUser.email()))
                .map(existingUser -> {
                    existingUser.setGoogleId(googleUser.googleId());
                    logger.debug("Updated existing user with Google ID for email={}", existingUser.getEmail());
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
                    logger.info("Created new user for email={}", newUser.getEmail());
                    return userRepository.save(newUser);

                });
    }

    // Skapar svar för en ny session med både access token och refresh token.
    private AuthResponseDTO createNewSessionResponse(User user){
        String accessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        logger.debug("Created new session response for userId={}", user.getUserId());

        return new AuthResponseDTO(
                accessToken,
                newRefreshToken.getToken(),
                jwtService.getExpiration()
        );
    }

    // Skapar svar vid token-refresh och återanvänder befintlig refresh token.
    private AuthResponseDTO createRefreshResponse(User user, RefreshToken existingToken) {
        String accessToken = jwtService.generateToken(user);
        logger.debug("Created refresh response for userId={}", user.getUserId());

        return new AuthResponseDTO(
                accessToken,
                existingToken.getToken(),
                jwtService.getExpiration());
    }


}
