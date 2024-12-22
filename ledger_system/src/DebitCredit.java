package sem1project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DebitCredit {

    public static double getBalance(int userId) {
        double balance = 0.0;
        Connection conn = DB.Connect();
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

    private static void updateBalance(int userId, double newBalance) {
        String sql = "UPDATE Balance SET current_amount = ? WHERE user_id = ?";
        try (Connection conn = DB.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }

    private static void insertTransaction(int userId, double amount, String description, String type) {
        String sql = "INSERT INTO Transactions (user_id, description, debit, credit, balance, transaction_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, description);

            if (type.equalsIgnoreCase("Debit")) {
                stmt.setDouble(3, amount); // Debit amount
                stmt.setDouble(4, 0.0);    // No credit
            } else {
                stmt.setDouble(3, 0.0);    // No debit
                stmt.setDouble(4, amount); // Credit amount
            }

            double currentBalance = getBalance(userId);
            double newBalance = type.equalsIgnoreCase("Debit") ? currentBalance - amount : currentBalance + amount;
            stmt.setDouble(5, newBalance); // Updated balance
            stmt.setString(6, type);

            stmt.executeUpdate();

            // Update the balance in the Balance table
            updateBalance(userId, newBalance);
        } catch (SQLException e) {
            System.out.println("Error inserting into Transactions: " + e.getMessage());
        }
    }

    public static void debitAmount(int userId, Scanner read) {
        System.out.println("\n=== DEBIT TRANSACTION ===");
        System.out.print("Enter amount to debit: ");
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

    public static void creditAmount(int userId, Scanner read) {
        System.out.println("\n=== CREDIT TRANSACTION ===");
        System.out.print("Enter amount to credit: ");
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

        insertTransaction(userId, amount, description, "Debit");
        System.out.println("Debit transaction successful!\n");
    }
}
