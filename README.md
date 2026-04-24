# Bank Management System using Java RMI

A distributed bank management system built with Java RMI and MySQL (via XAMPP). Multiple clients can connect to a single server and perform banking operations through a GUI.

## Description

| Module            | Description                                     |
| ----------------- | ----------------------------------------------- |
| `BankManager`     | RMI remote interface defining all operations    |
| `BankManagerImpl` | Server-side implementation of the interface     |
| `DatabaseManager` | Manages all MySQL database operations           |
| `LoginGUI`        | Login and registration screen                   |
| `BankManagerGUI`  | Main banking dashboard after login              |
| `Server`          | Starts the RMI registry and binds the service   |
| `Client`          | Connects to the RMI server and launches the GUI |

## Prerequisitess

- JDK 8 or higher
- XAMPP with MySQL running on port **3307**
- `mysql-connector-java-5.1.49.jar` (already in `lib/`)

---

## Running the Project

### On the Server Machine

**Step 1 — Start XAMPP MySQL**

- Open XAMPP Control Panel
- Start **Apache** and **MySQL**
- Make sure MySQL is running on port **3307**

**Step 2 — Create the database**

- Open [http://localhost/phpmyadmin](http://localhost/phpmyadmin)
- Create a new database named `bank_system`
- Tables are created automatically when the server starts

**Step 3 — Get your machine IP**

```cmd
ipconfig
```

Note your `IPv4 Address` e.g. `192.168.1.10`

**Step 4 — Compile**

```powershell
cd path\to\RMI_Java_SwingApplication
javac -cp ".;lib/mysql-connector-java-5.1.49.jar" *.java
```

**Step 5 — Start the Server (keep this terminal open)**

```powershell
java "-Djava.rmi.server.hostname=YOUR_IPv4" -cp ".;lib/mysql-connector-java-5.1.49.jar" Server
```

Expected output:

```
Database initialized successfully
Serveur RMI prêt.
```

---

### On Each Client Machine (same WiFi network)

**Step 1 — Clone the project**

```cmd
git clone https://github.com/sofoniasjohn8-a11y/distrubuted_banking_system_java.git
cd distrubuted_banking_system_java\RMI_Java_SwingApplication
```

**Step 2 — Compile**

```cmd
javac -cp ".;lib/mysql-connector-java-5.1.49.jar" *.java
```

**Step 3 — Run the client**

```cmd
java -cp ".;lib/mysql-connector-java-5.1.49.jar" Client YOUR_SERVER_IPv4 1099
```

Replace `YOUR_SERVER_IPv4` with the server machine IP from Step 3 above.

---

## Using the Application

1. **Register** — Enter your full name, username, and password
2. **Login** — Enter your username and password
3. **Create Account** — Enter an account ID and initial balance (only once per user)
4. **Deposit** — Enter account ID and amount, click Deposit
5. **Withdraw** — Enter account ID and amount, click Withdraw
6. **Transfer** — Enter amount, click Transfer, then enter source and destination account IDs
7. **Balance** — Enter account ID, click Balance to view current balance
8. **Logout** — Click Logout to return to the login screen

---

## Database Configuration

| Setting  | Value       |
| -------- | ----------- |
| Host     | localhost   |
| Port     | 3307        |
| Database | bank_system |
| Username | root        |
| Password | (empty)     |

To change these settings, edit the constants at the top of `DatabaseManager.java`.

---

## Troubleshooting

| Error                               | Fix                                                       |
| ----------------------------------- | --------------------------------------------------------- |
| `Port already in use: 1099`         | Run `taskkill /F /IM java.exe` then restart server        |
| `Communications link failure`       | XAMPP MySQL is not running                                |
| `Connection refused`                | Server is not running, start it first                     |
| `Unrecognized method hash`          | Recompile both server and client with `javac`             |
| `Could not find or load main class` | Wrong folder, `cd` into `RMI_Java_SwingApplication` first |

---

## Notes

- Only the **server machine** needs XAMPP/MySQL
- Client machines only need **JDK** installed
- Each user can only create **one account**
- All client operations go through RMI to the server
- Database persists across restarts
