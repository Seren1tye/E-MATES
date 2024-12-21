import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ledger {

    private static final String URL = "jdbc:mysql://localhost:3306/ledger_system"; 
    private static final String USER = "root";
    private static final String PASSWORD = "1234"; 

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static double getBalance(int userId) {
        double balance = 0.0;
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return balance;
        }

        String sql = "SELECT current_amount AS balance FROM balance WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    balance = rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
        return balance;
    }

    private static void insertTransaction(int userId, double amount, String description, String type) {
        String sql = "INSERT INTO Transactions (user_id, amount, description, transaction_date, transaction_type) " +
                     "VALUES (?, ?, ?, NOW(), ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.setString(3, description);
            stmt.setString(4, type);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting into Transactions: " + e.getMessage());
        }
    }

    private static void insertTransactionHistory(int userId, double amount, String description, String type, double balance) {
        String sql = "INSERT INTO TransactionsHistory (user_id, description, debit, credit, balance, date) " +
                     "VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, description);

            if (type.equals("Debit")) {
                stmt.setDouble(3, amount);
                stmt.setDouble(4, 0.0);
            } else {
                stmt.setDouble(3, 0.0);
                stmt.setDouble(4, amount);
            }

            stmt.setDouble(5, balance);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting into TransactionsHistory: " + e.getMessage());
        }
    }

    public static void debitAmount(int userId, Scanner read) {
        System.out.println("\n=== WITHDRAWAL TRANSACTION ===");
        System.out.print("Enter amount to withdraw: ");
        double amount = read.nextDouble();
        read.nextLine();

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.\n");
            return;
        }

        double currentBalance = getBalance(userId);

        if (amount > currentBalance) {
            System.out.println("Insufficient funds. Your current balance is: " + currentBalance + "\n");
            return;
        }

        System.out.print("Enter a description: ");
        String description = read.nextLine();

        insertTransaction(userId, -amount, description, "Debit");
        insertTransactionHistory(userId, -amount, description, "Debit", currentBalance - amount);

        System.out.println("Withdrawal transaction successful!\n");
    }

    public static void creditAmount(int userId, Scanner read) {
        System.out.println("\n=== CREDIT TRANSACTION ===");
        System.out.print("Enter amount to credit: ");
        double amount = read.nextDouble();
        read.nextLine();

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.\n");
            return;
        }

        System.out.print("Enter a description: ");
        String description = read.nextLine();

        double currentBalance = getBalance(userId);

        insertTransaction(userId, amount, description, "Credit");
        insertTransactionHistory(userId, amount, description, "Credit", currentBalance + amount);

        System.out.println("Credit transaction successful!\n");
    }
}
