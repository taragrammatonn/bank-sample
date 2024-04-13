package md.maib.controller;

import md.maib.entity.Customer;
import md.maib.service.CustomerService;
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

    @PutMapping("api/update-customer/{id}")
    public ResponseEntity<Customer> updateCustomerById(@PathVariable Long id, @RequestBody Customer customerUpdates) {
        Customer customer = customerService.updateCustomerById(id, customerUpdates);
        if (customer != null) {
            return ResponseEntity.ok(customer); // HTTP 200 OK
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404 Not Found
        }
    }
}


