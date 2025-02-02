# Automated ATM System

## Overview
This project models the structure of an automated ATM system with three main classes:
- **Bank**
- **Account**
- **ATM**

The system allows a bank to manage multiple accounts and ATMs. Accounts are linked to a bank, and ATMs provide access to account transactions.

## Features
### (a) Basic Class Structure
- **Bank**
    - Properties:
        - `name` (get, set)
        - `accounts` (get, multiple accounts)
        - `atms` (get, multiple ATMs)
    - Methods:
        - `add_account(account)`
        - `remove_account(account)`
        - `add_atm(atm)`

- **Account**
    - Properties:
        - `bank` (get, associated bank)
        - `account_number` (get, set)
        - `pin_code` (get, set)
        - `balance` (get)
    - Methods:
        - `replenish_account(amount)` – Add money to the account.
        - `withdraw_from_account(amount)` – Withdraw money from the account.

- **ATM**
    - Properties:
        - `bank` (get, associated bank)
        - `atm_id` (get, set)
        - `address` (get, set)
    - Methods:
        - `withdraw_money(pin_code, amount)` – Withdraw money from an account using a PIN.

### (b) Object Creation and Interaction
1. Create a **Bank** object.
2. Create **two Account** objects and associate them with the bank.
3. Create **three ATM** objects and associate them with the bank.
4. Display the status of one account.
5. Withdraw a specific amount from the account and display the updated status.

### (c) ATM Withdrawal Feature
- The ATM method `withdraw_money(pin_code, amount)` finds an account with the given PIN and withdraws the requested amount.
- The withdrawal process is validated through the bank.
- The main function tests this functionality by performing a withdrawal and displaying results.

## Running the Program
To test the functionality:
1. Create objects for the **Bank, Accounts, and ATMs# Automated ATM System

## Overview
This project models the structure of an automated ATM system with three main classes:
- **Bank**
- **Account**
- **ATM**

The system allows a bank to manage multiple accounts and ATMs. Accounts are linked to a bank, and ATMs provide access to account transactions.

## Features
### (a) Basic Class Structure
- **Bank**
    - Properties:
        - `name` (get, set)
        - `accounts` (get, multiple accounts)
        - `atms` (get, multiple ATMs)
    - Methods:
        - `add_account(account)`
        - `remove_account(account)`
        - `add_atm(atm)`

- **Account**
    - Properties:
        - `bank` (get, associated bank)
        - `account_number` (get, set)
        - `pin_code` (get, set)
        - `balance` (get)
    - Methods:
        - `replenish_account(amount)` – Add money to the account.
        - `withdraw_from_account(amount)` – Withdraw money from the account.

- **ATM**
    - Properties:
        - `bank` (get, associated bank)
        - `atm_id` (get, set)
        - `address` (get, set)
    - Methods:
        - `withdraw_money(pin_code, amount)` – Withdraw money from an account using a PIN.

### (b) Object Creation and Interaction
1. Create a **Bank** object.
2. Create **two Account** objects and associate them with the bank.
3. Create **three ATM** objects and associate them with the bank.
4. Display the status of one account.
5. Withdraw a specific amount from the account and display the updated status.

### (c) ATM Withdrawal Feature
- The ATM method `withdraw_money(pin_code, amount)` finds an account with the given PIN and withdraws the requested amount.
- The withdrawal process is validated through the bank.
- The main function tests this functionality by performing a withdrawal and displaying results.

## Running the Program
To test the functionality:
1. Create objects for the **Bank, Accounts, and ATMs
