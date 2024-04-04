package md.maib.repository;

import md.maib.config.JdbcConfig;
import md.maib.entity.Customer;
import md.maib.entity.PhoneNumber;
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
        String SQL = "SELECT users.*, phone_numbers.phone_id, phone_numbers.phone_number, transactions.transaction_id, transactions.amount, transactions.transaction_date " +
                "FROM users  " +
                "LEFT JOIN phone_numbers  ON users.user_id = phone_numbers.user_id " +
                "LEFT JOIN transactions  ON users.user_id = transactions.user_id " +
                "WHERE users.user_id = ?";

        Customer customer = null;
        Map<Long, PhoneNumber> phoneNumbers = new HashMap<>();
        Map<Long, Transaction> transactions = new HashMap<>();

        try (Connection conn = JdbcConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

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
                // Process phone numbers
                long phoneId = rs.getLong("phone_id");
                if (phoneId > 0 && !phoneNumbers.containsKey(phoneId)) {
                    PhoneNumber phoneNumber = new PhoneNumber.Builder()
                            .id(phoneId)
                            .phoneNumber(rs.getString("phone_number"))
                            .build();
                    phoneNumbers.put(phoneId, phoneNumber);
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
                customer.setPhoneNumbers(new ArrayList<>(phoneNumbers.values()));
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

            String deletePhoneNumbersSQL = "DELETE FROM phone_numbers WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deletePhoneNumbersSQL)) {
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