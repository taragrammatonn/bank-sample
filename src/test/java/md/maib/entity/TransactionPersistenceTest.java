package md.maib.entity;

import md.maib.entity.Customer;
import md.maib.entity.Transaction;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionPersistenceTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private CustomerRepository customerRepository;

    private Customer savedCustomer;

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
        savedCustomer = new Customer.Builder()
                .firstName("John")
                .lastName("Doe")
                .pan("1234567891011121")
                .cvv("123")
                .build();

        Transaction transaction = new Transaction(savedCustomer, new BigDecimal("100.00"), LocalDateTime.now());

        savedCustomer.setTransactions(List.of(transaction));
        customerRepository.save(savedCustomer);
    }

    @Test
    void shouldHaveCorrectTransactionAmount() {
        var retrievedCustomer = customerRepository.findById(savedCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        assertNotNull(retrievedCustomer.getTransactions(), "Customer should have one transaction");
        assertEquals(1, retrievedCustomer.getTransactions().size(), "Customer should have  only one transaction");

        Transaction firstTransaction = retrievedCustomer.getTransactions().get(0);

        assertNotNull(firstTransaction.getTransactionDate(), "Transaction should have a date");
        assertEquals(new BigDecimal("100.00"), firstTransaction.getAmount(), "Transaction amount should match");


    }
    @Test
    void shouldSetAndGetTransactionDetailsCorrectly() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("150.00"));
        transaction.setTransactionDate(LocalDateTime.of(2024, 3, 22, 12, 0));

        assertEquals(new BigDecimal("150.00"), transaction.getAmount(), "The transaction amount should match what was set");
        assertEquals(LocalDateTime.of(2024, 3, 22, 12, 0), transaction.getTransactionDate(), "The transaction date should match what was set");
    }
    @Test
    void shouldLinkTransactionToCustomerCorrectly() {
        Customer newCustomer = new Customer.Builder()
                .firstName("Alice")
                .lastName("Smith")
                .pan("9876543210123456")
                .cvv("321")
                .build();

        Transaction newTransaction = new Transaction(newCustomer, new BigDecimal("200.00"), LocalDateTime.now());

        newCustomer.setTransactions(List.of(newTransaction));
        customerRepository.save(newCustomer);

        Customer retrievedCustomer = customerRepository.findById(newCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Transaction retrievedTransaction = retrievedCustomer.getTransactions().get(0);

        assertNotNull(retrievedTransaction.getCustomer(), "Transaction should be linked to a customer");
        assertEquals(retrievedCustomer.getId(), retrievedTransaction.getCustomer().getId(), "Transaction should be linked to the correct customer");
    }
    @Test
    void shouldGetTransactionDateCorrectly() {
        Transaction transaction = new Transaction();
        LocalDateTime expectedDate = LocalDateTime.now();
        transaction.setTransactionDate(expectedDate);

        assertEquals(expectedDate, transaction.getTransactionDate(), "The transaction date should be correctly retrieved");
    }

    @Test
    void shouldSetAndGetTransactionIdCorrectly() {
        Transaction transaction = new Transaction();
        Long expectedId = 123L;
        transaction.setTransactionId(expectedId);

        assertEquals(expectedId, transaction.getTransactionId(), "The transaction ID should be correctly set and retrieved");
    }

    @Test
    void shouldSetCustomerCorrectly() {
        Transaction transaction = new Transaction();
        Customer expectedCustomer = new Customer.Builder()
                .firstName("Alice")
                .lastName("Smith")
                .pan("9876543210123456")
                .cvv("321")
                .build();
        transaction.setCustomer(expectedCustomer);

        assertEquals(expectedCustomer, transaction.getCustomer(), "The transaction should be correctly linked to the customer");
    }
}
