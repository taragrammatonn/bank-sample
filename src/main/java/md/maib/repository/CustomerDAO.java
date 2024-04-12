package md.maib.repository;

import md.maib.config.JdbcConfig;
import md.maib.entity.Customer;
import md.maib.entity.Transaction;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomerDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDAO.class);

    public Optional<Customer> getCustomerById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        //select all users and their transactions
        String SQL = "SELECT users.*, transactions.* from users " +
                "LEFT JOIN transactions ON users.user_id = transactions.user_id " +
                "WHERE users.user_id = ?";

        Customer customer = null;
        Map<Long, Transaction> transactions = new HashMap<>();

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (customer == null) { // Initialize customer once
                    customer = new Customer.Builder()
                            .id(rs.getLong("user_id"))
                            .firstName(rs.getString("first_name"))
                            .lastName(rs.getString("last_name"))
                            .pan(rs.getString("pan"))
                            .cvv(rs.getString("cvv"))
                            .age(rs.getInt("age")) // Assuming the Customer object has an age attribute
                            .build();
                }
                // Process transactions
                long transactionId = rs.getLong("transaction_id");
                if (transactionId > 0 && !transactions.containsKey(transactionId)) {
                    Transaction transaction = new Transaction.Builder()
                            .transactionId(transactionId)
                            .amount(rs.getBigDecimal("amount"))
                            .transactionDate(rs.getTimestamp("transaction_date").toLocalDateTime())
                            .build();
                    transactions.put(transactionId, transaction);
                }
            }
            if (customer != null) {
                customer.setTransactions(new ArrayList<>(transactions.values()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Get customer by ID from database failed", e);
            return Optional.empty();
        }
        return Optional.ofNullable(customer);
    }


    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = JdbcConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        String sql = "UPDATE users SET first_name = ?, last_name = ?, pan = ?, cvv = ? WHERE user_id = ?";
        String SQL = "UPDATE users SET first_name = ?, last_name = ?, pan = ?, cvv = ?, age = ? WHERE user_id = ?";

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

    public void deleteCustomerById(Long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
    public boolean deleteCustomerAndRelatedData(Long id) {
        Connection conn = null;
        try {
            conn = JdbcConfig.getConnection();
            conn.setAutoCommit(false);

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
