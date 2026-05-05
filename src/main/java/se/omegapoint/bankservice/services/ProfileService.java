package se.omegapoint.bankservice.services;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.ProfileRequestDTO;
import se.omegapoint.bankservice.dtos.ProfileUpdateDTO;
import se.omegapoint.bankservice.models.Profile;
import se.omegapoint.bankservice.repositories.CustomerRegisterRepository;

import java.math.BigDecimal;


@Singleton
public class ProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);

    private final CustomerRegisterRepository repository;
    public ProfileService(CustomerRegisterRepository repository) {
        this.repository = repository;
    }

    public Mono<Profile> createProfile(String userId, ProfileRequestDTO request) {

        LOG.info("Creating profile for userId {}", userId);

        Profile profile = new Profile(
                userId,
                request.firstName(),
                request.lastName(),
                request.socialSecurityNumber(),
                request.country(),
                request.city(),
                request.address(),
                request.zipCode(),
                request.phoneNumber(),
                request.yearlyIncome(),
                "ACTIVE"
        );
        return repository.saveProfile(profile)
                .doOnSuccess(saved -> LOG.debug("Created profile for userId={}", userId))
                .doOnError(e -> LOG.error("Error creating profile for userId={}", userId, e));
    }

    public Mono<Profile> getOrCreateProfile(String userId, String firstName, String lastName) {
        return repository.getUserInformation(userId)
                .switchIfEmpty(Mono.defer(() -> {
                    LOG.info("User {} does not exist, creating default profile", userId);
                    Profile newProfile = new Profile(
                            userId,
                            firstName,
                            lastName,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            BigDecimal.ZERO,
                            "ACTIVE"
                    );
                    return repository.saveProfile(newProfile);
                }))
                .doOnSuccess(profile -> LOG.debug("Found profile for userId={}", userId))
                .doOnError(e -> LOG.error("Error fetching/creating profile for userId={}", userId, e));
    }

    public Mono<Profile> getUserInformation(String userId) {
        return repository.getUserInformation(userId);
    }

    public Mono<Profile> updateProfileByUserId(String userId, ProfileUpdateDTO request) {

        LOG.info("Updating profile for userId {}", userId);

        return repository.findProfileByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Profile not found for userId: " + userId)))
                .flatMap(existing -> {
                    LOG.debug("Found existing profile for userId={}, applying updates", userId);
                    
                    if (request.firstName() != null) existing.setFirstName(request.firstName());
                    if (request.lastName() != null) existing.setLastName(request.lastName());
                    if (request.country() != null) existing.setCountry(request.country());
                    if (request.city() != null) existing.setCity(request.city());
                    if (request.address() != null) existing.setAddress(request.address());
                    if (request.zipCode() != null) existing.setZipCode(request.zipCode());
                    if (request.phoneNumber() != null) existing.setPhoneNumber(request.phoneNumber());
                    if (request.yearlyIncome() != null) existing.setYearlyIncome(request.yearlyIncome());
                    if (request.status() != null) existing.setStatus(request.status());

                    return repository.updateProfile(existing)
                            .doOnSuccess(updated -> LOG.debug("Successfully updated profile for userId={}", userId));
                })
                .doOnError(e -> LOG.error("Error updating profile for userId={}: {}", userId, e.getMessage()));
    }

    public Mono<Void> deleteProfileById(String userId) {

        LOG.info("Deleting profile for userId {}", userId);

        return repository.deleteProfileById(userId)
                .doOnSuccess(profile -> LOG.debug("Deleted profile for userId={}", userId))
                .doOnError(e -> LOG.error("Error deleting profile for userId={}", userId, e));
    }
}


