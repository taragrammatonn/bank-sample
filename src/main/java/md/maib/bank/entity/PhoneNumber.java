package md.maib.bank.entity;

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
    private String phone;

    public String getPhone() {
        return phone;
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

    public void setPhone(String phoneNumber) {
        this.phone = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneNumber that = (PhoneNumber) o;

        if (id != that.id) return false;
        if (!Objects.equals(customer, that.customer)) return false;
        return Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

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
            PhoneNumber phone = new PhoneNumber();
            phone.setId(this.id);
            phone.setPhone(this.phoneNumber);
            return phone;
        }
    }
}
