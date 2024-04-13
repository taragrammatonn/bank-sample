package md.maib.service.impl;

import md.maib.entity.Customer;
import md.maib.repository.CustomerDAO;
import md.maib.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerDAO.getCustomerById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    @Override
    public Customer updateCustomerById(Long id, Customer customerDetails) {
        return customerDAO.updateCustomerById(id, customerDetails);
    }

    @Override
    public void deleteCustomerAndRelatedData(Long id) {
        customerDAO.deleteCustomerAndRelatedData(id);
    }
}
