package md.maib.repository;

import md.maib.config.JdbcConfig;
import md.maib.entity.Customer;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomerDAO {
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
            e.printStackTrace();
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException(e);
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
            e.printStackTrace();
        }
        return  customerDetails;
    }

    public void deleteCustomerById(Long id) {
        String SQL = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, id);

            pstmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
