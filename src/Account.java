public class Account {
    private final String bankAccountNumber;
    private final String pinCode;
    private double balance;
    private final Bank bank;

    public Account(String bankAccountNumber, String pinCode, double balance, Bank bank) {
        this.bankAccountNumber = bankAccountNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.bank = bank;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public double getBalance() {
        return balance;
    }

    public Bank getBank() {
        return bank;
    }

    public void replenishAccount(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdrawFromAccount(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
