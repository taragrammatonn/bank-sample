package md.maib.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import md.maib.bank.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPan(String pan);

    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

}