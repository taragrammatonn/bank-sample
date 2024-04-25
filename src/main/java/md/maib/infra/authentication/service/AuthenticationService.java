package md.maib.infra.authentication.service;

import jakarta.websocket.EncodeException;
import md.maib.infra.authentication.dto.AuthenticationRequest;
import md.maib.infra.authentication.dto.AuthenticationResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthenticationService {

    Optional<AuthenticationResponse> loginUser(AuthenticationRequest customer) throws EncodeException;

}
