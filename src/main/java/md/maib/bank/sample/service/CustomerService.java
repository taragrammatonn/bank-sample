package md.maib.bank.sample.service;

import md.maib.bank.sample.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {

        Optional<Customer> getCustomerById(Long id);

        List<Customer> getAllCustomers();

        Customer updateCustomerById(Customer customerUpdates);

        void deleteCustomerAndRelatedData(Long id);

}
