package md.maib.bank.entity;

import md.maib.bank.entity.Customer;
import md.maib.bank.repository.CustomerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerPersistenceTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private final CustomerRepository customerRepository;

    public CustomerPersistenceTest(@Autowired CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private Customer repositorySavedCustomer = null;

    @BeforeEach
    public void initCustomer() {
        customerRepository.deleteAll();
        repositorySavedCustomer = new Customer.Builder()
                .firstName("John")
                .lastName("Doe")
                .pan("1234567891011121")
                .cvv("123")
                .age(25)
                .build();

        customerRepository.save(repositorySavedCustomer);
    }

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
    void shouldSelectCustomerById() {
        var savedCustomer = repositorySavedCustomer;

        var selectedCustomer = customerRepository.findById(savedCustomer.getId()).get();

        assertEquals(savedCustomer.getId(), selectedCustomer.getId());
    }

    @Test
    void shouldUpdateCustomerDetails() {
        var savedCustomer = repositorySavedCustomer;

        savedCustomer.setFirstName("UpdatedAlice");
        savedCustomer.setLastName("UpdatedSmith");
        var updatedCustomer = customerRepository.save(savedCustomer);

        assertEquals("UpdatedAlice", updatedCustomer.getFirstName());
        assertEquals("UpdatedSmith", updatedCustomer.getLastName());
    }

    @Test
    void shouldInsertNewCustomer() {
        var savedCustomer = repositorySavedCustomer;

        assertNotNull(savedCustomer.getId());
        assertEquals("John", savedCustomer.getFirstName());
        assertEquals("Doe", savedCustomer.getLastName());
        assertEquals("1234567891011121", savedCustomer.getPan());
        assertEquals("123", savedCustomer.getCvv());
    }

    @Test
    void shouldNotSaveCustomerWithDuplicatePan() {
        var customer2 = new Customer.Builder()
                .firstName("Bob")
                .lastName("Johnson")
                .pan("1234567891011121")
                .cvv("789")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            customerRepository.save(customer2);
        });
    }

    @Test
    void shouldReturnCorrectFullName() {
        var savedCustomer = repositorySavedCustomer;

        assertEquals("John Doe", savedCustomer.getFullName(), "Full name should match the concatenation of first and last names");
    }

    @Test
    void shouldBeEqual() {
        var savedCustomer = repositorySavedCustomer;
        var anotherCustomer = new Customer.Builder()
                .id(savedCustomer.getId())
                .firstName("John")
                .lastName("Doe")
                .pan("1234567891011121")
                .cvv("123")
                .age(25)
                .build();

        assertEquals(savedCustomer, anotherCustomer, "Both customers should be equal");
        assertEquals(savedCustomer.hashCode(), anotherCustomer.hashCode(), "Hash codes should match for equal objects");
    }
}