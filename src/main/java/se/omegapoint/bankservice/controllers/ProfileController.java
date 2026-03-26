package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.ProfileRequestDTO;
import se.omegapoint.bankservice.dtos.ProfileResponseDTO;
import se.omegapoint.bankservice.dtos.ProfileUpdateDTO;
import se.omegapoint.bankservice.mappers.ProfileMapper;
import se.omegapoint.bankservice.services.ProfileService;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/profile")
@Secured(IS_AUTHENTICATED)
public class ProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;
    public ProfileController(ProfileService profileService,  ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @Post
    public Mono<MutableHttpResponse<ProfileResponseDTO>> addProfile(@Body ProfileRequestDTO request, Authentication authentication) {
        String userId = authentication.getName();

        LOG.info("HTTP POST /profile for userId={}", userId);

        return profileService.createProfile(userId, request)
                .map(profileMapper::toResponseDto)
                .map(HttpResponse::created)
                .doOnSuccess(response -> LOG.debug("Created profile for userId={}", userId))
                .doOnError(e -> LOG.error("Error in POST /profile for userId={}", userId, e));
    }

    @Get
    public Flux<ProfileResponseDTO> getUserInformation(Authentication authentication) {
        String userId = authentication.getName();

        LOG.info("HTTP GET /profile for userId={}", userId);

        return profileService.getUserInformation(userId)
                .map(profileMapper::toResponseDto)
                .doOnNext(response -> LOG.debug("Returning profile response: {}", response))
                .doOnComplete(() -> LOG.debug("Completed GET /profile for userId={}", userId))
                .doOnError(e -> LOG.error("Error in GET /profile for userId={}", userId, e));
    }

    @Put("/{userId}/{socialSecurityNumber}")
    public Mono<MutableHttpResponse<ProfileResponseDTO>> updateProfile(
            @PathVariable String userId,
            @PathVariable String socialSecurityNumber,
            @Body ProfileUpdateDTO request) {

        LOG.info("HTTP PUT /profile/{} ", userId);

        return profileService.updateProfileBySocialSecurityNumber(userId, socialSecurityNumber, request)
                .map(profileMapper::toResponseDto)
                .map(HttpResponse::ok)
                .doOnSuccess(response -> LOG.debug("Updated profile for userId={}", userId))
                .doOnError(e -> LOG.error("Error in PUT /profile/{}", userId, e));
    }

    @Delete("/{userId}/{socialSecurityNumber}")
    public Mono<MutableHttpResponse<Void>> deleteProfile(
            @PathVariable String userId,
            @PathVariable String socialSecurityNumber) {

        LOG.info("HTTP DELETE /profile/{}", userId);

        return profileService.deleteProfileById(userId, socialSecurityNumber)
                .thenReturn(HttpResponse.<Void>noContent())
                .doOnSuccess(response -> LOG.debug("Deleted profile for userId={}", userId))
                .doOnError(e -> LOG.error("Error in DELETE /profile/{}", userId, e));
    }
}
