import java.util.ArrayList;
import java.util.List;

public class Bank {
    private final String name;
    private final List<Account> accounts;
    private final List<ATM> atms;

    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
        this.atms = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<ATM> getAtms() {
        return atms;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    public void addATM(ATM atm) {
        atms.add(atm);
    }

    public Account findAccountByPin(String pinCode) {
        for (Account account : accounts) {
            if (account.getPinCode().equals(pinCode)) {
                return account;
            }
        }
        return null;
    }
}
