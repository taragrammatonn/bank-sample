package md.maib.service.impl;

import md.maib.entity.Customer;
import md.maib.repository.CustomerDAO;
import md.maib.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    public CustomerServiceImpl( CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    private final CustomerDAO customerDAO;

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerDAO.getCustomerById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        try {
            return customerDAO.getAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer updateCustomerById(Long id, Customer customerDetails) {
        return customerDAO.updateCustomerById(id, customerDetails);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerDAO.deleteCustomerAndRelatedData(id);
    }
}
