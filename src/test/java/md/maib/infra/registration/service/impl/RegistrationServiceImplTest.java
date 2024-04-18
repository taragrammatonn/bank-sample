package md.maib.infra.registration.service.impl;

import jakarta.websocket.EncodeException;
import md.maib.infra.registration.dto.RegistrationRequest;
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
class RegistrationServiceImplTest {

    private RegistrationServiceImpl registrationService;

    @Autowired
    RegistrationServiceImplTest(RegistrationServiceImpl registrationService) {
        this.registrationService = registrationService;
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
    void shouldRegisterUser() throws EncodeException {
        var firstName = "Madalina";
        var lastName = "Rusu";
        var age = 25;

        var userRegistered = registrationService.registerCustomer(new RegistrationRequest(firstName, lastName, age));
        assertNotNull(userRegistered, "User should be registered");
    }
}