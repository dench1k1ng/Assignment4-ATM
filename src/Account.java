import java.sql.*;

public class Account {
    private int id;
    private String accountNumber;
    private String pinCode;
    private double balance;
    private int bankId;

    private static final String URL = "jdbc:postgresql://localhost:5432/bank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2603";

    public Account(String accountNumber, String pinCode, double balance, int bankId) {
        if (isAccountExists(accountNumber)) {
            throw new IllegalArgumentException("Account with number '" + accountNumber + "' already exists.");
        }
        this.accountNumber = accountNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.bankId = bankId;
        this.id = addAccountToDB();
    }

    // Database Connection
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Check if Account Exists
    private boolean isAccountExists(String accountNumber) {
        String sql = "SELECT id FROM accounts WHERE account_number = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking account existence: " + e.getMessage());
        }
        return false;
    }

    // Add Account to DB
    private int addAccountToDB() {
        String sql = "INSERT INTO accounts (account_number, pin_code, balance, bank_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, pinCode);
            pstmt.setDouble(3, balance);
            pstmt.setInt(4, bankId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("Error adding account: " + e.getMessage());
        }
        return -1;
    }

    public int getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds!");
            return;
        }
        balance -= amount;
        updateBalance();
    }

    public void deposit(double amount) {
        balance += amount;
        updateBalance();
    }

    private void updateBalance() {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, balance);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
        }
    }
}
