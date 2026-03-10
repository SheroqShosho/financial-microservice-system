package se.omegapoint.bankservice.services;
import se.omegapoint.bankservice.repositories.CustomerRegisterRepository;

public class UserService {

    private final CustomerRegisterRepository repository;

    public UserService(CustomerRegisterRepository customerRegisterRepository) {
        this.repository = customerRegisterRepository;
    }

}
