# ATM System

## Overview
This project implements a simple automated ATM system using Java. It models a banking system with the following classes:
- `Bank`: Represents a bank containing multiple accounts and ATMs.
- `Account`: Represents a bank account with a PIN code, balance, and associated bank.
- `ATM`: Represents an ATM that serves a specific bank and allows account holders to withdraw money.

## Features
- Create a bank with associated accounts and ATMs.
- Each account is linked to a specific bank and has a unique PIN code and balance.
- ATMs are linked to a bank and allow users to withdraw money using a valid PIN code.
- The balance of an account is updated when money is withdrawn successfully.

## Classes & Methods

### Bank Class
- `getName()`: Returns the name of the bank.
- `getAccounts()`: Returns the list of accounts.
- `getAtms()`: Returns the list of ATMs.
- `addAccount(Account account)`: Adds an account to the bank.
- `removeAccount(Account account)`: Removes an account from the bank.
- `addATM(ATM atm)`: Adds an ATM to the bank.
- `findAccountByPin(String pinCode)`: Finds an account by PIN code.

### Account Class
- `getBankAccountNumber()`: Returns the account number.
- `getPinCode()`: Returns the PIN code.
- `getBalance()`: Returns the account balance.
- `getBank()`: Returns the associated bank.
- `replenishAccount(double amount)`: Adds funds to the account.
- `withdrawFromAccount(double amount)`: Withdraws funds from the account.

### ATM Class
- `getIdentificationNumber()`: Returns the ATM ID.
- `getAddress()`: Returns the ATM location.
- `getBank()`: Returns the associated bank.
- `withdrawMoney(String pinCode, double amount)`: Withdraws money from an account using a PIN code.

## Usage
1. Create a `Bank` object.
2. Create `Account` objects and add them to the bank.
3. Create `ATM` objects and add them to the bank.
4. Withdraw money using an ATM and verify the balance updates correctly.

## Example Execution
```java
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
```

