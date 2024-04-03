package md.maib.service.impl;

import md.maib.entity.Customer;
import md.maib.service.CustomerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceImplTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private CustomerService customerService;

    @BeforeAll
    static void beforeAll() {
        postgres.withInitScript("schema_test.sql");
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
    private Customer prepareUpdatedCustomerDetails() {
        return new Customer.Builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .pan("4321")
                .cvv("765")
                .age(25)
                .build();
    }

    @Test
    void shouldGetAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        assertFalse(customers.isEmpty(), "The customer list should not be empty");
        assertEquals(2, customers.size(), "The customer list should contain exactly one customer");
    }

    @Test
    void shouldGetCustomerById() {
        var retrievedCustomer = customerService.getCustomerById(1L);

        assertNotNull(retrievedCustomer, "Customer should not be null");
    }

    @Test
    void shouldUpdateCustomerById() {
        var updatedDetails = prepareUpdatedCustomerDetails();
        var updatedCustomer = customerService.updateCustomerById(1L, updatedDetails);

        assertEquals(updatedDetails, updatedCustomer, "Customer details should be updated");
    }

    @Test
    void shouldDeleteCustomerById() {
        customerService.deleteCustomerById(1L);

        var expected = customerService.getAllCustomers();
        assertEquals(1,expected.size(), "Customer should be deleted");
  }
}