
package md.maib.repository;

import md.maib.config.JdbcConfig;
import md.maib.entity.Address;
import md.maib.entity.Customer;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomerDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDAO.class);

    public Optional<Customer> getCustomerById(Long id) {
        String SQL = "SELECT users.*, addresses.address_id, addresses.street, addresses.city, addresses.state, addresses.zip_code, addresses.country " +
                "FROM users LEFT JOIN addresses ON users.user_id = addresses.user_id " +
                "WHERE users.user_id = ?";

        Customer customer = null;

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            List<Address> addresses = new ArrayList<>();

            while (rs.next()) {
                if (customer == null) { // Initialize customer and its direct properties once
                    customer = new Customer.Builder()
                            .id(rs.getLong("user_id"))
                            .firstName(rs.getString("first_name"))
                            .lastName(rs.getString("last_name"))
                            .pan(rs.getString("pan"))
                            .cvv(rs.getString("cvv"))
                            .age(rs.getInt("age"))
                            .build();
                }
                // Check if the address exists (not all customers might have addresses)
                long addressId = rs.getLong("address_id");
                if (addressId > 0) { // This assumes address_id is not nullable; adjust accordingly
                    Address address = new Address.Builder()
                            .id(addressId)
                            .street(rs.getString("street"))
                            .city(rs.getString("city"))
                            .state(rs.getString("state"))
                            .zipCode(rs.getString("zip_code"))
                            .country(rs.getString("country"))
                            .build();
                    addresses.add(address);
                }
            }
            if (customer != null) {
                customer.setAddresses(addresses); // Set the addresses to the customer
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Get customer by ID from database failed", e);
            return Optional.empty();
        }
        return Optional.ofNullable(customer);
    }


    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String SQL = "SELECT * FROM users";

        try (Connection conn = JdbcConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                Customer customer = new Customer.Builder()
                        .id(rs.getLong("user_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .pan(rs.getString("pan"))
                        .cvv(rs.getString("cvv"))
                        .age(rs.getInt("age"))
                        .build();
                customers.add(customer);
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Get all customers operation failed", e);
        }
        return customers;
    }

    public Customer updateCustomerById(Long id, Customer customerDetails) {
        String SQL = "UPDATE users SET first_name = ?, last_name = ?, pan = ?, cvv = ?, age = ? WHERE user_id = ?";

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, customerDetails.getFirstName());
            pstmt.setString(2, customerDetails.getLastName());
            pstmt.setString(3, customerDetails.getPan());
            pstmt.setString(4, customerDetails.getCvv());
            pstmt.setInt(5, customerDetails.getAge());
            pstmt.setLong(6, id);

            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Update customer operation failed", e);
        }
        return customerDetails;
    }

    public boolean deleteCustomerAndRelatedData(Long id) {
        Connection conn = null;
        try {
            conn = JdbcConfig.getConnection();
            conn.setAutoCommit(false);

            String deleteAddressesSQL = "DELETE FROM addresses WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteAddressesSQL)) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
            }

            String deleteTransactionSQL = "DELETE FROM transactions WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteTransactionSQL)) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
            }

            String deleteCustomerSQL = "DELETE FROM users WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteCustomerSQL)) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
            }

            conn.commit(); // Transaction commit if all operations succeed
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Transaction rollback on error
                } catch (SQLException ex) {
                    LOGGER.error("Rollback failed", ex);
                }
            }
            LOGGER.error("Delete customer and related data operation failed", e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                } catch (SQLException e) {
                    LOGGER.error("Auto-commit reset failed", e);
                }
            }
        }
    }
}
