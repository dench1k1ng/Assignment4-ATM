import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private int id;
    private String name;

    private static final String URL = "jdbc:postgresql://localhost:5432/bank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2603";

    // Constructor for an existing bank (fetch from DB)
    public Bank(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor to create a new bank in the database
    public Bank(String name) {
        if (isBankExists(name)) {
            throw new IllegalArgumentException("Bank with name '" + name + "' already exists.");
        }
        this.name = name;
        this.id = addBankToDB(name);
    }

    // Database connection helper
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Check if a bank with the given name exists
    private boolean isBankExists(String name) {
        String sql = "SELECT id FROM banks WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a bank with this name exists
        } catch (SQLException e) {
            System.err.println("Error checking bank existence: " + e.getMessage());
        }
        return false;
    }

    // Add bank to database and return generated ID
    private int addBankToDB(String name) {
        String sql = "INSERT INTO banks (name) VALUES (?) RETURNING id";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error adding bank: " + e.getMessage());
        }
        return -1; // Return -1 if failed
    }

    // Fetch all banks from database
    public static List<Bank> getAllBanks() {
        List<Bank> banks = new ArrayList<>();
        String sql = "SELECT id, name FROM banks";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                banks.add(new Bank(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching banks: " + e.getMessage());
        }
        return banks;
    }

    // Delete a bank by name
    public static void deleteBank(String name) {
        String sql = "DELETE FROM banks WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
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

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }

    // Set name (Update DB)
    public void setName(String newName) {
        if (isBankExists(newName)) {
            System.out.println("Bank with name '" + newName + "' already exists.");
            return;
        }

        String sql = "UPDATE banks SET name = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            this.name = newName;
            System.out.println("Bank name updated to: " + newName);
        } catch (SQLException e) {
            System.err.println("Error updating bank name: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Bank [ID: " + id + ", Name: " + name + "]";
    }
}
