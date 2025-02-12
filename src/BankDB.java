import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/bank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2603";

    // Establish connection to database
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection connectToDatabase() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // Insert a new bank
    public void addBank(String name) {
        String sql = "INSERT INTO banks (name) VALUES (?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Bank added: " + name);
        } catch (SQLException e) {
            System.err.println("Error adding bank: " + e.getMessage());
        }
    }

    public void addAccount(String accountNumber, String pin, double balance, Optional<Integer> bankId) {
        String sql = "INSERT INTO accounts (account_number, pin_code, balance, bank_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, pin);
            pstmt.setDouble(3, balance);
            if (bankId.isPresent()) {
                pstmt.setInt(4, bankId.get());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);  // Handle null bank_id
            }
            pstmt.executeUpdate();
            System.out.println("Account added: " + accountNumber);
        } catch (SQLException e) {
            System.err.println("Error adding account: " + e.getMessage());
        }
    }



    public Optional<Integer> getBankIdByName(String bankName) {
        if (bankName == null || bankName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank name cannot be null or empty.");
        }

        String sql = "SELECT id FROM banks WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bankName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getInt("id")); // Return the bank ID wrapped in Optional
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bank ID: " + e.getMessage());
        }
        return Optional.empty(); // Return an empty Optional if not found or an error occurs
    }


    // Get all banks
    public List<String> getBanks() {
        List<String> banks = new ArrayList<>();
        String sql = "SELECT name FROM banks";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                banks.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching banks: " + e.getMessage());
        }
        return banks;
    }

    // Delete a bank
    public void removeBank(String name) {
        String sql = "DELETE FROM banks WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Bank removed: " + name);
            } else {
                System.out.println("No bank found with name: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Error removing bank: " + e.getMessage());
        }
    }
}
