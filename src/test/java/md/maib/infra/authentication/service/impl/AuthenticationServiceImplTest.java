package md.maib.infra.authentication.service.impl;

import jakarta.websocket.EncodeException;
import md.maib.infra.authentication.dto.AuthenticationRequest;
import md.maib.infra.authentication.service.AuthenticationService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationServiceImplTest {

    private final AuthenticationService authenticationService;

    @Autowired
    AuthenticationServiceImplTest(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldFindUserByPanAndCvv() throws EncodeException {
        var pan = "1234567890123456";
        var cvv = "123";

        var userFound = authenticationService.loginUser(new AuthenticationRequest(pan, cvv));
        assertNotNull(userFound, "User should be found");
    }

    @Test
    void shouldNotFindUserByPanAndCvv() throws EncodeException {
        var pan = "1234567890123456";
        var cvv = "1234";

        var userFound = authenticationService.loginUser(new AuthenticationRequest(pan, cvv));
        assertNotNull(userFound, "User should not be found");
    }
}