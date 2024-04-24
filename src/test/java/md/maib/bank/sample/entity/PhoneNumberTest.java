package md.maib.bank.sample.entity;

import md.maib.bank.sample.mother.AbstractContainerBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
class PhoneNumberTest extends AbstractContainerBaseTest {

    private Customer customer;

    private PhoneNumber phoneNumber;

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
        phoneNumber.setPhone(newPhoneNumber);
        assertEquals(newPhoneNumber, phoneNumber.getPhone(), "Setter or getter for phoneNumber not working as expected");
    }

}
