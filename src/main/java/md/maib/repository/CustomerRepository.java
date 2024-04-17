package md.maib.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import md.maib.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}