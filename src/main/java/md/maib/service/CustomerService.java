package md.maib.service;

import md.maib.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {

        Optional<Customer> getCustomerById(Long id);

        List<Customer> getAllCustomers();

        Customer updateCustomerById(Long id, Customer customerDetails);

        void deleteCustomerById(Long id);

}
