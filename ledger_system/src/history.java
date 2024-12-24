import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class history {
    public static void mainHistory(int userId) {
        try (Connection conn = DB.Connect()) {
            if (conn == null) {
                System.out.println("Unable to establish a database connection. Exiting...");
                return;
            }

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n== Transaction Menu ==");
                System.out.println("1. View Transaction History");
                System.out.println("2. Export to CSV");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        viewTransactionHistory(userId);
                        break;
                    case 2:
                        exportToCSV(userId);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view transaction history
     private static void viewTransactionHistory(int userId) throws SQLException {
        // Establish a connection to the database
        try (Connection conn = DB.Connect()) {
            if (conn == null) {
                System.out.println("Unable to establish a database connection. Exiting...");
                return;
            }

            // Query for the transactions based on the provided userId
            String query = "SELECT date, description, debit, credit, balance FROM Transactions WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId); // Set user ID in the query
                try (ResultSet rs = stmt.executeQuery()) {
                    System.out.printf("%-12s %-20s %-10s %-10s %-10s%n", "Date", "Description", "Debit", "Credit", "Balance");
                    System.out.println("-------------------------------------------------------------");
                    boolean hasData = false;

                    // Iterate through the result set and display the transactions
                    while (rs.next()) {
                        hasData = true;
                        String date = rs.getString("date");
                        String description = rs.getString("description");
                        double debit = rs.getDouble("debit");
                        double credit = rs.getDouble("credit");
                        double balance = rs.getDouble("balance");

                        System.out.printf("%-12s %-20s %-10.2f %-10.2f %-10.2f%n",
                                date, description, debit, credit, balance);
                    }

                    if (!hasData) {
                        System.out.println("No transaction history found for the given User ID.");
                    }
                }
            }
        }
    }

    // Method to export transaction history to CSV
    private static void exportToCSV(int userId) throws SQLException {
        // Establish a connection to the database
        try (Connection conn = DB.Connect()) {
            if (conn == null) {
                System.out.println("Unable to establish a database connection. Exiting...");
                return;
            }

            String query = "SELECT date, description, debit, credit, balance FROM Transactions WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId); // Set user ID in the query
                try (ResultSet rs = stmt.executeQuery();
                     FileWriter writer = new FileWriter("TransactionHistory_User" + userId + ".csv")) {

                    // Write CSV headers
                    writer.append("Date,Description,Debit,Credit,Balance\n");

                    boolean hasData = false;

                    // Write data to CSV
                    while (rs.next()) {
                        hasData = true;
                        String date = rs.getString("date");
                        String description = rs.getString("description");
                        double debit = rs.getDouble("debit");
                        double credit = rs.getDouble("credit");
                        double balance = rs.getDouble("balance");

                        writer.append(String.format("%s,%s,%.2f,%.2f,%.2f\n",
                                date, description, debit, credit, balance));
                    }

                    if (hasData) {
                        System.out.println("Transaction history exported to TransactionHistory_User" + userId + ".csv");
                    } else {
                        System.out.println("No transaction history found for the given User ID.");
                    }
                } catch (IOException e) {
                    System.out.println("Error writing to CSV file: " + e.getMessage());
                }
            }
        }
    }
}