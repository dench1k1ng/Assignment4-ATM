public class ATM {
    private final String identificationNumber;
    private final String address;
    private final Bank bank;

    public ATM(String identificationNumber, String address, Bank bank) {
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.bank = bank;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getAddress() {
        return address;
    }

    public Bank getBank() {
        return bank;
    }

    public boolean withdrawMoney(String pinCode, double amount) {
        Account account = bank.findAccountByPin(pinCode);
        if (account != null) {
            return account.withdrawFromAccount(amount);
        }
        return false;
    }
}