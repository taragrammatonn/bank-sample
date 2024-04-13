package md.maib.repository;

import md.maib.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    Optional<Customer> getCustomerById(Long id);

    List<Customer> getAllCustomers();

    Customer updateCustomerById(Long id, Customer customerDetails);

    void deleteCustomerAndRelatedData(Long id);
}
