package md.maib.bank.controller;

import md.maib.bank.entity.Customer;
import md.maib.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("api/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("api/customers/{id}")
    public ResponseEntity<String> getCustomerbyId(@PathVariable Long id) {
        Optional<Customer> customerOptional = customerService.getCustomerById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            String firstName = customer.getFirstName();
            return ResponseEntity.ok(firstName);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }


    @DeleteMapping("api/delete-customer/{id}")
    public ResponseEntity<Integer> deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerAndRelatedData(id);
        return ResponseEntity.ok().build();
    }

   @PutMapping("api/update-customer")
    public ResponseEntity<Customer> updateCustomerById(@RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomerById(customer);
        return ResponseEntity.ok(updatedCustomer);
    }
}


