package md.maib.bank.sample.repository;

import md.maib.bank.sample.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPan(String pan);

    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

}