import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;

public class BankGUI extends JFrame {
    private static final String URL = "jdbc:postgresql://localhost:5432/bank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2603";
    private BankDB bankDB;


    private JComboBox<String> bankList;
    private JTextField bankNameField, accountNumberField, pinField, amountField, atmIdField, atmAddressField, accountBalanceField;
    private JButton addBankBtn, deleteBankBtn, addAccountBtn, depositBtn, withdrawBtn, addATMBtn;

    private Map<String, Integer> bankMap = new HashMap<>();  // Store bank names and their IDs

    public BankGUI() {
        setTitle("Banking System");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        this.bankDB = new BankDB();
        Connection conn = bankDB.connectToDatabase();
        if (conn != null) {
            System.out.println("Connected to the database!");
        } else {
            System.out.println("Failed to connect.");
        }

        // Panel for Bank Operations
        JPanel bankPanel = new JPanel(new FlowLayout());
        bankNameField = new JTextField(10);
        addBankBtn = new JButton("Add Bank");
        deleteBankBtn = new JButton("Delete Bank");
        bankList = new JComboBox<>();
        loadBanks();

        bankPanel.add(new JLabel("Bank Name:"));
        bankPanel.add(bankNameField);
        bankPanel.add(addBankBtn);
        bankPanel.add(deleteBankBtn);
        bankPanel.add(new JLabel("Select Bank:"));
        bankPanel.add(bankList);

        // Panel for Account Operations
        JPanel accountPanel = new JPanel(new FlowLayout());
        accountNumberField = new JTextField(10);
        accountBalanceField = new JTextField(10);
        pinField = new JTextField(4);
//        amountField = new JTextField(7);
        addAccountBtn = new JButton("Create Account");
        depositBtn = new JButton("Deposit");
        withdrawBtn = new JButton("Withdraw");

        accountPanel.add(new JLabel("Account No:"));
        accountPanel.add(accountNumberField);
        accountPanel.add(new JLabel("PIN:"));
        accountPanel.add(pinField);
        accountPanel.add(addAccountBtn);
        accountPanel.add(new JLabel("Amount:"));
        accountPanel.add(accountBalanceField);
        accountPanel.add(depositBtn);
        accountPanel.add(withdrawBtn);

        // Panel for ATM Operations
        JPanel atmPanel = new JPanel(new FlowLayout());
        atmIdField = new JTextField(7);
        atmAddressField = new JTextField(15);
        addATMBtn = new JButton("Add ATM");

        atmPanel.add(new JLabel("ATM ID:"));
        atmPanel.add(atmIdField);
        atmPanel.add(new JLabel("Address:"));
        atmPanel.add(atmAddressField);
        atmPanel.add(addATMBtn);

        // Add panels to Frame
        getContentPane().add(bankPanel);
        getContentPane().add(accountPanel);
        getContentPane().add(atmPanel);


        // Button Actions
        addBankBtn.addActionListener(e -> addBank());
        deleteBankBtn.addActionListener(e -> deleteBank());
        addAccountBtn.addActionListener(e -> addAccount());
        depositBtn.addActionListener(e -> depositMoney());
        withdrawBtn.addActionListener(e -> withdrawMoney());
        addATMBtn.addActionListener(e -> addATM());

        setVisible(true);
    }


    // Database Connection
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Load Banks into JComboBox
    private void loadBanks() {
        bankList.removeAllItems();
        bankMap.clear();  // Clear the map before loading

        List<Bank> banks = Bank.getAllBanks();
        for (Bank bank : banks) {
            bankList.addItem(bank.getName());
            bankMap.put(bank.getName(), bank.getId());
        }
    }

    // Fetch all banks
    private List<String> getBanks() {
        List<String> banks = new ArrayList<>();
        String sql = "SELECT name FROM banks";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                banks.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching banks: " + e.getMessage());
        }
        return banks;
    }

    // Add a new bank
    private void addBank() {
        String name = bankNameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bank name cannot be empty.");
            return;
        }
        String sql = "INSERT INTO banks (name) VALUES (?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bank added!");
            loadBanks();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding bank: " + e.getMessage());
        }
    }

    // Delete a bank
    private void deleteBank() {
        String name = (String) bankList.getSelectedItem();
        if (name == null) {
            JOptionPane.showMessageDialog(this, "No bank selected.");
            return;
        }
        String sql = "DELETE FROM banks WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bank deleted!");
            loadBanks();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting bank: " + e.getMessage());
        }
    }

    // Add Account
    private void addAccount() {
        String accountNumber = accountNumberField.getText().trim(); // Trim whitespace
        String pin = pinField.getText().trim();
        String balanceText = accountBalanceField.getText().trim();
        String selectedBank = (String) bankList.getSelectedItem();  // Get the selected bank name

        // Debugging: Print values
        System.out.println("Account Number: " + accountNumber);
        System.out.println("PIN: " + pin);
        System.out.println("Balance: " + balanceText);

        // Check if any field is empty
        if (accountNumber.isEmpty() || pin.isEmpty() || balanceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double balance = Double.parseDouble(balanceText); // Convert balance to number
            if (balance < 0) {
                JOptionPane.showMessageDialog(this, "Balance cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Optional<Integer> bankId = bankDB.getBankIdByName(selectedBank); // Get bank ID
            bankDB.addAccount(accountNumber, pin, balance, bankId); // Save to database
            JOptionPane.showMessageDialog(this, "Account added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid balance input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void addATM() {
        String atmAddress = atmAddressField.getText().trim();
        String selectedBank = (String) bankList.getSelectedItem();

        if (atmAddress.isEmpty() || selectedBank == null) {
            JOptionPane.showMessageDialog(this, "Fill all fields and select a bank.");
            return;
        }

        int bankId = bankMap.get(selectedBank);  // Get the bank ID from the map

        try {
            ATM newATM = new ATM(atmAddress, bankId);
            JOptionPane.showMessageDialog(this, "ATM added successfully! ATM ID: " + newATM.getAtmId());
            atmAddressField.setText("");  // Clear the address field
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    // Deposit Money
    private void depositMoney() {
        updateBalance(true);
    }

    // Withdraw Money
    private void withdrawMoney() {
        updateBalance(false);
    }

    // Update Balance
    private void updateBalance(boolean isDeposit) {
        String accountNumber = accountNumberField.getText().trim();
        String amountStr = accountBalanceField.getText().trim();  // Assume this holds the amount for deposit/withdrawal
        String selectedBank = (String) bankList.getSelectedItem();  // Get selected bank name

        if (accountNumber.isEmpty() || amountStr.isEmpty() || selectedBank == null) {
            JOptionPane.showMessageDialog(this, "All fields are required, and a bank must be selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Optional<Integer> bankId = bankDB.getBankIdByName(selectedBank);  // Get bank ID
            if (bankId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bank not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = isDeposit ?
                    "UPDATE accounts SET balance = balance + ? WHERE account_number = ? AND bank_id = ?" :
                    "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND bank_id = ? AND balance >= ?";

            try (Connection conn = bankDB.connectToDatabase();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, amount);
                pstmt.setString(2, accountNumber);
                pstmt.setInt(3, bankId.get());
                if (!isDeposit) {
                    pstmt.setDouble(4, amount);  // For withdrawal, ensure balance is sufficient
                }

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Transaction successful!");
                } else {
                    JOptionPane.showMessageDialog(this, isDeposit ? "Account not found." : "Insufficient funds or account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount input!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        new BankGUI();
    }
}
