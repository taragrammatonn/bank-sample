package md.maib.entity;

import md.maib.repository.CustomerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressPersistanceTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private CustomerRepository customerRepository;

    private Customer savedCustomer;

    private Address savedAddress;

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

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();

        var customer = new Customer.Builder()
                .firstName("John")
                .lastName("Doe")
                .pan("1234567891011121")
                .cvv("123")
                .build();

        savedAddress = new Address.Builder()
                .city("Chisinau")
                .country("Moldova")
                .state("Chisinau")
                .street("Stefan cel Mare")
                .zipCode("2001")
                .build();

        savedAddress.setCustomer(customer);
        customer.setAddresses(List.of(savedAddress));

        savedCustomer = customerRepository.save(customer);
    }


    @Test
    void shouldSaveCustomerWithAddress() {
        var selectedCustomer = customerRepository.findById(savedCustomer.getId()).get();

        assertNotNull(selectedCustomer);
        assertFalse(selectedCustomer.getAddresses().isEmpty());
    }

    @Test
    void addressGetters() {
        assertEquals("Chisinau", savedAddress.getCity(), "Getter for city failed");
        assertEquals("Moldova", savedAddress.getCountry(), "Getter for country failed");
        assertEquals("Chisinau", savedAddress.getState(), "Getter for state failed");
        assertEquals("Stefan cel Mare", savedAddress.getStreet(), "Getter for street failed");
        assertEquals("2001", savedAddress.getZipCode(), "Getter for zipCode failed");
    }

    //check hashCode and equals
    @Test
    void testEqualsAndHashCode() {
        assertEquals(savedAddress, savedAddress, "Address should be equal to itself");

        assertEquals(savedAddress.hashCode(), savedAddress.hashCode(), "Hash code should be consistent");

    }

}
