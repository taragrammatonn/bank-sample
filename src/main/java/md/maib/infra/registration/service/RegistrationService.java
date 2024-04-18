package md.maib.infra.registration.service;

import jakarta.websocket.EncodeException;
import md.maib.infra.registration.dto.RegistrationRequest;
import md.maib.infra.registration.dto.RegistrationResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RegistrationService {
    Optional<RegistrationResponse> registerCustomer(RegistrationRequest request) throws EncodeException;
}
