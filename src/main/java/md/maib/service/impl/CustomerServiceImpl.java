package md.maib.service.impl;

import md.maib.entity.Customer;
import md.maib.repository.CustomerRepository;
import md.maib.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomerById(Customer customerDetails) {
        return customerRepository.save(customerDetails);
    }

    @Override
    public void deleteCustomerAndRelatedData(Long id) {
        customerRepository.deleteById(id);
    }
}
