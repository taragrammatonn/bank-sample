package md.maib.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import md.maib.entity.Customer;

import java.util.List;
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Override
    List<Customer> findAll();

}
