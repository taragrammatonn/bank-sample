package md.maib.infra.registration.service.impl;

import jakarta.websocket.EncodeException;
import md.maib.bank.config.impl.AESCoderImpl;
import md.maib.bank.entity.Customer;
import md.maib.bank.repository.CustomerRepository;
import md.maib.infra.registration.dto.RegistrationRequest;
import md.maib.infra.registration.dto.RegistrationResponse;
import md.maib.infra.registration.generator.CVVGenerator;
import md.maib.infra.registration.generator.PANGenerator;
import md.maib.infra.registration.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final PANGenerator panGenerator;
    private final CVVGenerator cvvGenerator;
    private final AESCoderImpl securityService;

    @Autowired
    public RegistrationServiceImpl(CustomerRepository customerRepository, PANGenerator panGenerator, CVVGenerator cvvGenerator, AESCoderImpl securityService) {
        this.customerRepository = customerRepository;
        this.panGenerator = panGenerator;
        this.cvvGenerator = cvvGenerator;
        this.securityService = securityService;
    }

    @Override
    public Optional<RegistrationResponse> registerCustomer(RegistrationRequest customer) throws EncodeException {
        var userFound = customerRepository.findByFirstNameAndLastName(customer.getFirstName(), customer.getLastName());

        if (userFound.isPresent()) {
            LOGGER.info("User already exists with first name: {} and last name: {}", customer.getFirstName(), customer.getLastName());
            return Optional.empty();
        }

        var pan = panGenerator.generatePAN();
        var cvv = cvvGenerator.generateCVV();

        pan = securityService.encode(pan);
        cvv = securityService.encode(cvv);

        LOGGER.info("Registering new user with first name: {} and last name: {}", customer.getFirstName(), customer.getLastName());
        var userEntity = customerRepository.save(new Customer.Builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .pan(pan)
                .cvv(cvv)
                .age(customer.getAge())
                .build());

        return Optional.of(new RegistrationResponse(userEntity.getFirstName(), userEntity.getLastName(), pan, userEntity.getCvv()));
    }
}
