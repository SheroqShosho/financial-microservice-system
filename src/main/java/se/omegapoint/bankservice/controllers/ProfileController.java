package se.omegapoint.bankservice.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.ProfileRequestDTO;
import se.omegapoint.bankservice.dtos.ProfileResponseDTO;
import se.omegapoint.bankservice.mappers.ProfileMapper;
import se.omegapoint.bankservice.services.ProfileService;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller
@Secured(IS_ANONYMOUS)
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;
    public ProfileController(ProfileService profileService,  ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @Post("/profile")
    public Mono<HttpResponse<ProfileResponseDTO>> addProfile(@Body ProfileRequestDTO request) {

        String testUserId = "test123";

        return profileService.createProfile(testUserId, request)
                .map(profileMapper::toResponseDto)
                .map(HttpResponse::created);

    }

}
