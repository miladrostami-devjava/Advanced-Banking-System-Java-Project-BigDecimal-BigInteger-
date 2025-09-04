





# Advanced Banking System — Java Project (BigDecimal & BigInteger)

![Java](https://img.shields.io/badge/Java-17+-blue)
![License](https://img.shields.io/badge/License-MIT-green)

## 🎯 Project Overview

**Advanced Banking System** is a full-featured educational banking project implemented in Java, demonstrating the power
 of **`BigDecimal`** and **`BigInteger`** for financial calculations and cryptography. The system is designed to
 cover real-world banking scenarios while providing accurate numeric computations, atomic transactions, and secure operations.

Key features include:

- Creation of bank accounts with **very large account numbers** using `BigInteger`.
- **Precise financial operations** using `BigDecimal`, including deposits, withdrawals, and transfers.
- **Atomic and thread-safe money transfers** with fees and currency conversion.
- **Multi-currency support** (USD, EUR, IRR, JPY) with a currency conversion module.
- **Loan amortization schedule** computation using `BigDecimal` for accurate interest calculations.
- **RSA key pair generation** and simple encryption/decryption with `BigInteger`.
- **Transaction logging** for each account.
- **Persistence**: Save and load accounts to/from a file (JSON or serialized format).
- Console-based menu-driven application for user interaction.

This project is ideal for **teaching Java**, **financial programming**, and **BigDecimal/BigInteger usage** in real applications.

---

## 🏗️ Project Structure

```

src/
├─ org.bank/
│  ├─ MainApp.java           # Entry point of the program
│  ├─ model/
│  │  ├─ Account.java        # Bank account class
│  │  ├─ Currency.java       # Supported currencies
│  │  ├─ Transaction.java    # Transaction record
│  ├─ service/
│  │  ├─ BankingService.java # Core banking operations
│  │  ├─ LoanCalculator.java # Loan calculation & amortization
│  │  ├─ CurrencyConverter.java
│  │  └─ PersistenceService.java
│  ├─ util/
│  │  ├─ HelperPrint.java    # Utility for console output
│  │  └─ RSAUtils.java       # RSA key generation & encryption
└─ resources/

````

---

## ⚙️ Features & Usage

### 1. Account Management
- Create account with large `BigInteger` account number.
- Deposit and withdraw precise amounts using `BigDecimal`.
- Supports multiple currencies with thread-safe operations.

### 2. Transfer with Fee & Conversion
- Atomic transfer between accounts.
- Fee computation using `BigDecimal`.
- Automatic currency conversion when source and destination accounts differ in currency.
- Transaction logging with timestamps.

### 3. Loan Amortization
- Calculate monthly payments using precise interest rates.
- Generate full loan amortization schedules for any principal, rate, and duration.

### 4. RSA Encryption
- Generate RSA key pairs using `BigInteger`.
- Encrypt and decrypt messages securely.
- Demonstrates real-world cryptographic operations with large integers.

### 5. Persistence
- Save all accounts and transactions to a file.
- Load accounts on program start.

---

## 💻 Running the Project

### Prerequisites
- Java 21+
- IDE: IntelliJ IDEA, Eclipse, or any Java IDE
- Optional: Git for version control

### Compile & Run

```bash
# Clone the repository
git clone https://github.com/miladrostami-devjava/Advanced-Banking-System-Java-Project-BigDecimal-BigInteger-.git
cd AdvancedBankingSystem

# Compile Java files
javac -d out src/org/bank/**/*.java

# Run MainApp
java -cp out org.bank.MainApp
````

---

## 📝 Example Menu

```
=== Banking System Menu ===
1. Create Account
2. Deposit
3. Withdraw
4. Transfer
5. Show Accounts
6. Currency Conversion
7. Loan Amortization Schedule
8. Encrypt Demo
9. Save Account To File
0. Exit
```

* Select an option to perform banking operations interactively.
* Enter amounts with decimals, account IDs as `BigInteger`, and currencies (USD, EUR, IRR, JPY).

---

## 🔐 Technical Highlights

* **`BigDecimal`**: Accurate representation of currency amounts, avoiding floating-point errors.
* **`BigInteger`**: Extremely large account numbers, RSA keys, and encrypted messages.
* **Thread safety**: `synchronized` blocks for account operations.
* **Atomic transfers**: Prevents partial state updates.
* **RSA Utils**: Key generation, encryption, decryption, signing.
* **Loan calculator**: Precise monthly interest calculations and amortization schedule generation.

---

## 🛠️ Contributing

Contributions are welcome! You can:

* Add more currencies
* Implement GUI interface
* Add more cryptographic features
* Enhance persistence (database support)

Please fork the repository and submit pull requests.

---

## 📜 License

This project is licensed under the **MIT License** — see [LICENSE](LICENSE) for details.

---

## 👨‍🏫 Author

* **Milad Rostami (Armin)**
* Email: `miladrostami24@gmail.com`
* Location: Poland/Iran

---

## ⚡ Learning Goals

* Mastering `BigDecimal` and `BigInteger` in Java
* Implementing thread-safe banking operations
* Understanding real-world financial computations
* Learning basic RSA cryptography
* Handling data persistence and logging in Java

