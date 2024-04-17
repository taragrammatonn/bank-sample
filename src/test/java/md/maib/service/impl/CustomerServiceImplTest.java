package md.maib.service.impl;

import md.maib.entity.Customer;
import md.maib.mother.AbstractContainerBaseTest;
import md.maib.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceImplTest extends AbstractContainerBaseTest {

    private CustomerService customerService;

    @Autowired
    CustomerServiceImplTest(CustomerService customerService) {
        this.customerService = customerService;
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

        assertNotNull(retrievedCustomer, "Customer should be retrieved");
    }

    @Test
    void shouldUpdateCustomerById() {
        var updatedDetails = prepareUpdatedCustomerDetails();
        var updatedCustomer = customerService.updateCustomerById(updatedDetails);

        assertEquals(updatedDetails, updatedCustomer, "Customer details should be updated");
    }

    @Test
    void shouldDeleteCustomerById() {
        customerService.deleteCustomerAndRelatedData(1L);

        var expected = customerService.getAllCustomers();
        assertEquals(1, expected.size(), "Customer should be deleted");
    }
}
