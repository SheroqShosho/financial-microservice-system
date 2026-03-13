package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.ProfileRequestDTO;
import se.omegapoint.bankservice.dtos.ProfileResponseDTO;
import se.omegapoint.bankservice.dtos.ProfileUpdateDTO;
import se.omegapoint.bankservice.mappers.ProfileMapper;
import se.omegapoint.bankservice.services.ProfileService;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller("/profile")
@Secured(IS_ANONYMOUS)
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;
    public ProfileController(ProfileService profileService,  ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @Post
    public Mono<HttpResponse<ProfileResponseDTO>> addProfile(@Body ProfileRequestDTO request) {

        String testUserId = "test123";

        return profileService.createProfile(testUserId, request)
                .map(profileMapper::toResponseDto)
                .map(HttpResponse::created);

    }

    @Get
    public Flux<ProfileResponseDTO> getUserInformation() {

        String testUserId = "test123";

        return profileService.getUserInformation(testUserId)
                .map(profileMapper::toResponseDto);
    }

    @Put("/{userId}/{socialSecurityNumber}")
    public Mono<HttpResponse<ProfileResponseDTO>> updateProfile(
            @PathVariable String userId,
            @PathVariable String socialSecurityNumber,
            @Body ProfileUpdateDTO request) {

        return profileService.updateProfileBySocialSecurityNumber(userId, socialSecurityNumber, request)
                .map(profileMapper::toResponseDto)
                .map(HttpResponse::ok);

    }

    @Delete("/{userId}/{socialSecurityNumber}")
    public Mono<HttpResponse<Void>> deleteProfile(
            @PathVariable String userId,
            @PathVariable String socialSecurityNumber) {
        return profileService.deleteProfileById(userId, socialSecurityNumber)
                .thenReturn(HttpResponse.noContent());
    }
}
