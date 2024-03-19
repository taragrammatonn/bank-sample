package md.maib.service;

import md.maib.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    Customer getCustomerById(Long id);
    List<Customer> getAllCustomers();
    Customer updateCustomerById(Long id, Customer customerDetails);
    void deleteCustomerById(Long id);

}
