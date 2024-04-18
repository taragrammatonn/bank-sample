package md.maib.infra.authentication.service.impl;

import jakarta.websocket.EncodeException;
import md.maib.bank.config.impl.AESCoderImpl;
import md.maib.bank.repository.CustomerRepository;
import md.maib.infra.authentication.service.AuthenticationService;
import md.maib.infra.authentication.dto.AuthenticationRequest;
import md.maib.infra.authentication.dto.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final CustomerRepository customerRepository;

    private final AESCoderImpl securityService;

    @Autowired
    public AuthenticationServiceImpl(CustomerRepository customerRepository, AESCoderImpl securityService) {
        this.customerRepository = customerRepository;
        this.securityService = securityService;
    }

    @Override
    public Optional<AuthenticationResponse> loginUser(AuthenticationRequest request) throws EncodeException {

        String pan = securityService.encode(request.getPan());
        String cvv = securityService.encode(request.getCvv());

        LOGGER.info("Login user with pan: {}", request.getPan());
        return customerRepository.findByPan(pan)
                .filter(customer -> customer.getCvv().equals(cvv))
                .map(customer -> new AuthenticationResponse(customer.getFirstName(), customer.getLastName(), customer.getPan()));
    }
}