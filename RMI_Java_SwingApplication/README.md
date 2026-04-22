# Bank Management System using Java RMI

This project is a bank management system implemented using Java's Remote Method Invocation (RMI) framework with MySQL database persistence (via XAMPP). It allows clients to perform operations such as creating an account, depositing money, withdrawing money, checking balance, and transferring funds between accounts through a graphical user interface (GUI).

## Description

The bank management system is divided into several modules:

- `Account`: A class representing a bank account with basic attributes like account ID and balance.
- `BankManager`: An RMI remote interface that defines the operations that can be performed on bank accounts.
- `BankManagerImpl`: An implementation of the `BankManager` interface, providing the logic for account operations using database persistence.
- `DatabaseManager`: A singleton class that manages all MySQL database operations for account persistence.
- `BankManagerGUI`: A `JFrame` based GUI that interacts with the `BankManager` to perform operations and display results to the user.
- `Client`: A client class that looks up the RMI registry for the `BankManager` service and initializes the GUI.
- `Server`: Sets up the RMI registry and binds the `BankManagerImpl` object to make it available to clients.

## Installation

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- XAMPP with MySQL running on port 3307
- MySQL JDBC driver (`mysql-connector-java-*.jar` or `mysql-connector-j-*.jar`)

### Setup Steps

1. **Start XAMPP and MySQL**:
   - Open XAMPP Control Panel
   - Start Apache and MySQL services
   - Ensure MySQL is running on port 3307

2. **Create the Database**:
   - Open phpMyAdmin (http://localhost/phpmyadmin)
   - Create a new database named `bank_system`
   - The tables will be created automatically when the server starts

3. **Download MySQL JDBC driver**:
   - Download `mysql-connector-j-*.jar` from [MySQL](https://dev.mysql.com/downloads/connector/j/)
   - Or use `mysql-connector-java-8.0.33.jar` (older version)
   - Place it in your project directory (e.g., `lib/mysql-connector-j-8.0.33.jar`)

4. **Compile the Java files with MySQL JDBC driver**:

   ```bash
   javac -cp lib/mysql-connector-j-8.0.33.jar *.java
   ```

5. **Start the RMI registry** (in a separate terminal):

   ```bash
   rmiregistry
   ```

6. **Ensure XAMPP/MySQL is running** on port 3307

7. **Start the RMI registry** in a separate terminal:

   ```bash
   rmiregistry
   ```

8. **Start the server** in another terminal:

   ```bash
   java -cp .;lib/mysql-connector-j-8.0.33.jar Server
   ```

9. **Run the client** in a separate terminal to interact with the server through the GUI:

   ```bash
   java -cp .;lib/mysql-connector-j-8.0.33.jar Client
   ```

10. Run the client in a separate terminal to interact with the server through the GUI:

    ```bash
    java -cp .:sqlite-jdbc-3.44.0.0.jar Client
    ```

11. Use the graphical user interface to perform operations:
    - **Create Account**: Enter account ID and initial balance, then click "Create Account"
    - **Deposit**: Enter account ID and amount, then click "Deposit"
    - **Withdraw**: Enter account ID and amount to withdraw, then click "Withdraw"
    - **Check Balance**: Enter account ID and click "Check Balance"
    - **Transfer**: Click "Transfer", enter source and destination account IDs, and specify the amount in the amount field

## Database

The system Configuration

The system is configured to connect to MySQL with the following details:

- **Host**: localhost
- **Port**: 3307
- **Database**: bank_system
- **Username**: root
- **Password**: (empty)

To change these settings, edit `DatabaseManager.java` and modify the connection constants at the top of the class.

### Database Schema

The system uses a single table that is created automatically:

```sqlMySQL database via XAMPP
- **RMI Communication**: Client-server communication using Java RMI
- **Remote Interface**: BankManager interface defines all remote operations
- **Error Handling**: Comprehensive error handling for invalid operations
- **GUI Interface**: User-friendly Swing-based GUI for bank operations
- **Account Validation**: Validates account existence and sufficient balance before operations
- **Decimal Precision**: Uses DECIMAL(15,2) for accurate monetary calculations

## Notes

- Ensure XAMPP MySQL is running before starting the server
- The database persists across application restarts
- All monetary operations are validated for sufficient balance
- Account IDs must be unique
- Database will be created automatically on first server start
- Tables will be initialized automatically if they don't existhensive error handling for invalid operations
- **GUI Interface**: User-friendly Swing-based GUI for bank operations
- **Account Validation**: Validates account existence and sufficient balance before operations

## Notes

- The database persists across application restarts
- All monetary operations are validated for sufficient balance
- Account IDs must be unique
```
3Cj3ClraTknduH7gyW5LolUWAS1_e8fBpGVahcxxfAyRoNTA
