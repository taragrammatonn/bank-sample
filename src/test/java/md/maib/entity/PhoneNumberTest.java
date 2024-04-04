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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PhoneNumberTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private Customer customer;

    private PhoneNumber phoneNumber;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        phoneNumber = new PhoneNumber.Builder()
                .id(1L)
                .phoneNumber("123-456-7890")
                .build();
        phoneNumber.setCustomer(customer);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void testEqualsAndHashCode() {
        var anotherPhoneNumber = new PhoneNumber.Builder()
                .id(1L)
                .phoneNumber("123-456-7890")
                .build();
        anotherPhoneNumber.setCustomer(customer);

        assertEquals(phoneNumber, anotherPhoneNumber, "Both phone numbers should be equal");
        assertEquals(phoneNumber.hashCode(), anotherPhoneNumber.hashCode(), "Hash codes should match for equal objects");

        anotherPhoneNumber.setId(2L);
        assertNotEquals(phoneNumber, anotherPhoneNumber, "Phone numbers with different IDs should not be equal");
    }

    @Test
    void testConstructor() {
        assertNotNull(phoneNumber, "PhoneNumber should be successfully instantiated");
    }

    @Test
    void testSetAndGetId() {
        phoneNumber.setId(2L);
        assertEquals(2L, phoneNumber.getId(), "Setter or getter for ID not working as expected");
    }

    @Test
    void testSetAndGetCustomer() {
        var anotherCustomer = new Customer(); // Adjust based on Customer class design
        anotherCustomer.setId(2L);
        phoneNumber.setCustomer(anotherCustomer);
        assertEquals(anotherCustomer, phoneNumber.getCustomer(), "Setter or getter for Customer not working as expected");
    }

    @Test
    void testSetAndGetPhoneNumber() {
        var newPhoneNumber = "098-765-4321";
        phoneNumber.setPhoneNumber(newPhoneNumber);
        assertEquals(newPhoneNumber, phoneNumber.getPhoneNumber(), "Setter or getter for phoneNumber not working as expected");
    }

}