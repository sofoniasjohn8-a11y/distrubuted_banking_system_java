import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL      = "jdbc:mysql://localhost:3307/bank_system";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";
    private static DatabaseManager instance;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    private DatabaseManager() { initializeDatabase(); }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "username VARCHAR(50) PRIMARY KEY," +
                "fullname VARCHAR(100) NOT NULL," +
                "password VARCHAR(100) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
            );
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "username VARCHAR(50) NOT NULL," +
                "somme DECIMAL(15,2) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (username) REFERENCES users(username))"
            );
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public boolean accountExists(String id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM accounts WHERE id=?")) {
            ps.setString(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    public boolean userHasAccount(String username) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM accounts WHERE username=?")) {
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    public String getAccountIdByUsername(String username) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM accounts WHERE username=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("id");
        } catch (SQLException e) { /* ignore */ }
        return "";
    }

    public void createAccount(String id, double somme, String username) throws Exception {
        if (userHasAccount(username)) throw new Exception("You already have an account. Only one account is allowed per user.");
        if (accountExists(id)) throw new Exception("Account ID '" + id + "' is already taken. Please choose a different ID.");
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO accounts (id, username, somme) VALUES (?,?,?)")) {
            ps.setString(1, id);
            ps.setString(2, username);
            ps.setDouble(3, somme);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error creating account: " + e.getMessage());
        }
    }

    public double getBalance(String id) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT somme FROM accounts WHERE id=?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("somme");
            throw new Exception("Account '" + id + "' does not exist. Please check the account ID.");
        } catch (SQLException e) {
            throw new Exception("Error retrieving balance: " + e.getMessage());
        }
    }

    public void updateBalance(String id, double newBalance) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET somme=? WHERE id=?")) {
            ps.setDouble(1, newBalance);
            ps.setString(2, id);
            if (ps.executeUpdate() == 0) throw new Exception("Account " + id + " not found.");
        } catch (SQLException e) {
            throw new Exception("Error updating balance: " + e.getMessage());
        }
    }

    public void deposit(String id, double amount) throws Exception {
        if (!accountExists(id)) throw new Exception("Account '" + id + "' does not exist. Please check the account ID.");
        if (amount <= 0) throw new Exception("Deposit amount must be greater than zero.");
        updateBalance(id, getBalance(id) + amount);
    }

    public void withdraw(String id, double amount) throws Exception {
        if (!accountExists(id)) throw new Exception("Account " + id + " not found.");
        double bal = getBalance(id);
        if (bal < amount) throw new Exception("Insufficient balance. Current: " + bal);
        updateBalance(id, bal - amount);
    }

    public void transfer(String fromId, String toId, double amount) throws Exception {
        if (fromId == null || fromId.trim().isEmpty()) throw new Exception("Please enter the source account ID.");
        if (toId == null || toId.trim().isEmpty())     throw new Exception("Please enter the destination account ID.");
        if (!accountExists(fromId)) throw new Exception("Source account '" + fromId + "' does not exist.");
        if (!accountExists(toId))   throw new Exception("Destination account '" + toId + "' does not exist.");
        if (fromId.equals(toId))    throw new Exception("Source and destination accounts cannot be the same.");
        if (amount <= 0)            throw new Exception("Transfer amount must be greater than zero.");
        double fromBal = getBalance(fromId);
        if (fromBal < amount) throw new Exception("Insufficient balance in source account. Available: $" + String.format("%.2f", fromBal) + ".");
        updateBalance(fromId, fromBal - amount);
        updateBalance(toId, getBalance(toId) + amount);
    }

    public boolean userExists(String username) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE username=?")) {
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    public void registerUser(String username, String fullname, String password) throws Exception {
        if (userExists(username)) throw new Exception("User '" + username + "' already exists.");
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, fullname, password) VALUES (?,?,?)")) {
            ps.setString(1, username);
            ps.setString(2, fullname);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error registering user: " + e.getMessage());
        }
    }

    public boolean authenticateUser(String username, String password) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    public String getFullname(String username) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT fullname FROM users WHERE username=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("fullname");
        } catch (SQLException e) { /* ignore */ }
        return username;
    }
}
