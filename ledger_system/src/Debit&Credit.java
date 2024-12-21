import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ledger {


    public static double getBalance(int userId) {
        double balance = 0.0;
        Connection conn = DB.connect();
        if (conn == null) {
            System.out.println("Failed to connect to the database.");
            return balance;
        }

        String sql = "SELECT current_amount AS balance FROM Balance WHERE user_id = ?";
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
        String sql = "INSERT INTO Transactions (user_id, description, debit, credit, balance, transaction_type) " +
                     "VALUES (?, ?, ?, ?, (SELECT current_amount FROM Balance WHERE user_id = ?) + ?, ?)";
        try (Connection conn = DB.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, description);

            if (type.equalsIgnoreCase("Debit")) {
                stmt.setDouble(3, amount);
                stmt.setDouble(4, 0.0);
                stmt.setDouble(5, userId);
                stmt.setDouble(6, -amount);
            } else {
                stmt.setDouble(3, 0.0);
                stmt.setDouble(4, amount);
                stmt.setDouble(5, userId);
                stmt.setDouble(6, amount);
            }

            stmt.setString(7, type);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting into Transactions: " + e.getMessage());
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

        insertTransaction(userId, amount, description, "Credit");
        System.out.println("Credit transaction successful!\n");
    }
}
