package se.omegapoint.bankservice.services;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.omegapoint.bankservice.dtos.ProfileRequestDTO;
import se.omegapoint.bankservice.models.Profile;
import se.omegapoint.bankservice.repositories.CustomerRegisterRepository;


@Singleton
public class ProfileService {

    private final CustomerRegisterRepository repository;
    public ProfileService(CustomerRegisterRepository repository) {
        this.repository = repository;
    }

    public Mono<Profile> createProfile(String userId, ProfileRequestDTO request) {
        Profile profile = new Profile(
                userId,
                request.socialSecurityNumber(),
                request.country(),
                request.city(),
                request.address(),
                request.zipCode(),
                request.phoneNumber(),
                request.yearlyIncome(),
                "ACTIVE"
        );
        return repository.saveProfile(profile);
    }

    public Flux<Profile> getUserInformation(String userId) {

        return repository.getUserInformation(userId);
    }

}


