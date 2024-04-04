package md.maib.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "phone_numbers")
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Customer customer;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    public static class Builder {
        private long id;
        private String phoneNumber;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public PhoneNumber build() {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setId(this.id);
            phoneNumber.setPhoneNumber(this.phoneNumber);
            return phoneNumber;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneNumber that = (PhoneNumber) o;

        if (id != that.id) return false;
        if (!Objects.equals(customer, that.customer)) return false;
        return Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }
}
