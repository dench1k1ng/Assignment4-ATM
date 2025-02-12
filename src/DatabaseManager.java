import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/bank";
    private static final String USER = "posgtres";
    private static final String PASSWORD = "2603";
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBank(String name) {
        String query = "INSERT INTO banks (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAccount(String accountNumber, String pin, double balance, int bankId) {
        String query = "INSERT INTO accounts (account_number, pin_code, balance, bank_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, pin);
            stmt.setDouble(3, balance);
            stmt.setInt(4, bankId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addATM(String atmId, String address, int bankId) {
        String query = "INSERT INTO atms (atm_identification_number, address, bank_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, atmId);
            stmt.setString(2, address);
            stmt.setInt(3, bankId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdrawMoney(String accountNumber, String pin, double amount) {
        String query = "SELECT balance FROM accounts WHERE account_number = ? AND pin_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance >= amount) {
                    String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setDouble(1, amount);
                        updateStmt.setString(2, accountNumber);
                        updateStmt.executeUpdate();
                        System.out.println("Withdrawal successful. New balance: $" + (currentBalance - amount));
                    }
                } else {
                    System.out.println("Insufficient funds.");
                }
            } else {
                System.out.println("Account not found or invalid PIN.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void replenishAccount(String accountNumber, double amount) {
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();
            System.out.println("Account replenished successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
