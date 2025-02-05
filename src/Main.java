public class Main {
    public static void main(String[] args) {
        Bank myBank = new Bank("National Bank");

        Account acc1 = new Account("123456789", "1111", 5000, myBank);
        Account acc2 = new Account("987654321", "2222", 3000, myBank);

        myBank.addAccount(acc1);
        myBank.addAccount(acc2);

        ATM atm1 = new ATM("ATM001", "123 Main St", myBank);
        ATM atm2 = new ATM("ATM002", "456 Elm St", myBank);
        ATM atm3 = new ATM("ATM003", "789 Oak St", myBank);

        myBank.addATM(atm1);
        myBank.addATM(atm2);
        myBank.addATM(atm3);

        System.out.println("Баланс аккаунта: " + acc1.getBalance());

        boolean success = atm1.withdrawMoney("1111", 1000);
        System.out.println("Снятие 1000: " + (success ? "успешно" : "недостаточно средств"));

        System.out.println("Баланс аккаунта после снятия: " + acc1.getBalance());
    }
}