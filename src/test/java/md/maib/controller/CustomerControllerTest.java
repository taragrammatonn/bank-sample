package md.maib.controller;

import md.maib.entity.Customer;
import md.maib.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Customer prepareUpdatedCustomerDetails() {
        return new Customer.Builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .pan("4321")
                .cvv("765")
                .build();
    }

    @Test
    void shouldGetCustomerById() {
        Customer customer = new Customer.Builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pan("1234567890123456")
                .cvv("123")
                .build();


        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        ResponseEntity<String> result = customerController.getCustomerbyId(1L);

        assertEquals("John", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void shouldGetAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer.Builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .pan("4321")
                .cvv("765")
                .build());
        customers.add(new Customer.Builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .pan("1234")
                .cvv("987")
                .build());

        when(customerService.getAllCustomers()).thenReturn(customers);

        List<Customer> result = customerController.getAllCustomers();

        assertEquals(2, result.size());
    }

    @Test
    void shouldDeleteCustomerById() {
        ResponseEntity<Integer> responseEntity = customerController.deleteCustomerById(1L);

        verify(customerService, times(1)).deleteCustomerById(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldUdateCustomerById() {
        Customer updateCustomer = new Customer.Builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pan("1234567890123456")
                .cvv("123")
                .build();

        when(customerService.updateCustomerById(1L,updateCustomer)).thenReturn(updateCustomer);

        ResponseEntity<Customer> updateResult = customerController.updateCustomerById(1L, updateCustomer);

        assertEquals("John", updateResult.getBody().getFirstName());
        assertEquals(HttpStatus.OK, updateResult.getStatusCode());
    }
    @Test
    void shouldBeEqual() {

        Customer originalCustomer = new Customer.Builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pan("1234567890123456")
                .cvv("123")
                .build();

        Customer expectedCustomer = new Customer.Builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pan("1234567890123456")
                .cvv("123")
                .build();

        assertEquals(originalCustomer, expectedCustomer);
    }

    @Test
    void testHashCode(){
        Customer Customer = new Customer.Builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pan("1234567890123456")
                .cvv("123")
                .build();

        Customer anotherCustomer = new Customer.Builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .pan("1234567890123456")
                .cvv("123")
                .build();

        assertEquals(Customer,anotherCustomer , "Both addresses should be equal");
        assertEquals(Customer.hashCode(), anotherCustomer.hashCode(), "Hash codes should match for equal objects");

        anotherCustomer.setFirstName("Mihai");
        assertNotEquals("Addresses with different cities should not be equal", Customer, anotherCustomer);
    }
}
