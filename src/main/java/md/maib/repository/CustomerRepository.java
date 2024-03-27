package md.maib.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import md.maib.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer getCustomerById(Long id);

   void deleteCustomerById(Long id);
}
