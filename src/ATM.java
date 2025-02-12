import java.sql.*;

public class ATM {
    private int atmId;
    private String address;
    private int bankId;

    private static final String URL = "jdbc:postgresql://localhost:5432/bank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2603";

    public ATM(String address, int bankId) {
        if (isATMExists(address, bankId)) {
            throw new IllegalArgumentException("An ATM already exists at this location for this bank.");
        }
        this.address = address;
        this.bankId = bankId;
        this.atmId = addATMToDB(address, bankId);
    }

    // Database Connection
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Check if ATM Exists
    private boolean isATMExists(String address, int bankId) {
        String sql = "SELECT atm_id FROM atms WHERE address = ? AND bank_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address);
            pstmt.setInt(2, bankId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking ATM existence: " + e.getMessage());
        }
        return false;
    }

    // Add ATM to DB and return the generated atm_id
    private int addATMToDB(String address, int bankId) {
        String sql = "INSERT INTO atms (address, bank_id) VALUES (?, ?) RETURNING atm_id";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address);
            pstmt.setInt(2, bankId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("atm_id");
            }
        } catch (SQLException e) {
            System.err.println("Error adding ATM: " + e.getMessage());
        }
        throw new RuntimeException("Failed to add ATM to the database.");
    }

    // Getters
    public int getAtmId() { return atmId; }
    public String getAddress() { return address; }
    public int getBankId() { return bankId; }
}
