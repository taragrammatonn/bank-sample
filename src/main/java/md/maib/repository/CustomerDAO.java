package md.maib.repository;

import md.maib.config.JdbcConfig;
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
        String SQL = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer.Builder()
                        .id(rs.getLong("user_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .pan(rs.getString("pan"))
                        .cvv(rs.getString("cvv"))
                        .build();
                return Optional.of(customer);
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Get customer by ID from database failed", e);
        }
        return Optional.empty();
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
                        .build();
                customers.add(customer);
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Get all customers operation failed", e);
        }
        return customers;
    }

    public Customer updateCustomerById(Long id, Customer customerDetails) {
        String SQL = "UPDATE users SET first_name = ?, last_name = ?, pan = ?, cvv = ? WHERE user_id = ?";

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, customerDetails.getFirstName());
            pstmt.setString(2, customerDetails.getLastName());
            pstmt.setString(3, customerDetails.getPan());
            pstmt.setString(4, customerDetails.getCvv());
            pstmt.setLong(5, id);

            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Update customer operation failed", e);
        }
        return customerDetails;
    }

    public void deleteCustomerById(Long id) {
        String SQL = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, id);

            pstmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Delete customer operation failed", e);
        }
    }
}
