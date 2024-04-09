package md.maib.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "cvv", nullable = false)
    private String cvv;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "pan", nullable = false, unique = true, length = 16)
    private String pan;

    @Column(name = "age", nullable = false)
    private int age;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    public int getAge() {
        return age;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(cvv, customer.cvv) && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(pan, customer.pan) && Objects.equals(age, customer.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cvv, firstName, lastName, pan, age);
    }

    public static class Builder {

        private Long id;
        private String cvv;
        private String firstName;
        private String lastName;
        private String pan;
        private int age;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder cvv(String cvv) {
            this.cvv = cvv;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder pan(String pan) {
            this.pan = pan;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.setId(id);
            customer.setCvv(cvv);
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setPan(pan);
            customer.setAge(age);
            return customer;
        }
    }

}
