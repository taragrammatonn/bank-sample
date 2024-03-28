package md.maib.Controller;

import md.maib.entity.Customer;
import md.maib.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("api/customers")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("api/customers/{id}")
    public Customer getCustomerbyId(@PathVariable Long id){
        return customerService.getCustomerById(id).orElse(null);
    }

    @DeleteMapping("api/delete-customer/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable Long id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("api/update-customer/{id}")
    public Customer updateCustomerById(@PathVariable Long id, @RequestBody Customer customerUpdates) {
        return customerService.updateCustomerById(id, customerUpdates);
    }
}
