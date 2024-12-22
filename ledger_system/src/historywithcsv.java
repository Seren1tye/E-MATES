import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class historywithcsv {
    // JDBC URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/BankAliff"; 
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "12345678"; // Replace with your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n== Transaction Menu ==");
                System.out.println("1. View Transaction History");
                System.out.println("2. Export to CSV");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        viewTransactionHistory(conn);
                        break;
                    case 2:
                        exportToCSV(conn);
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
    private static void viewTransactionHistory(Connection conn) throws SQLException {
        String query = "SELECT date, description, debit, credit, balance FROM TransactionsHistory";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.printf("%-12s %-20s %-10s %-10s %-10s%n", "Date", "Description", "Debit", "Credit", "Balance");
            System.out.println("-------------------------------------------------------------");
            while (rs.next()) {
                String date = rs.getString("date");
                String description = rs.getString("description");
                double debit = rs.getDouble("debit");
                double credit = rs.getDouble("credit");
                double balance = rs.getDouble("balance");

                System.out.printf("%-12s %-20s %-10.2f %-10.2f %-10.2f%n",
                        date, description, debit, credit, balance);
            }
        }
    }

    // Method to export transaction history to CSV
    private static void exportToCSV(Connection conn) throws SQLException {
        String query = "SELECT date, description, debit, credit, balance FROM TransactionsHistory";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             FileWriter writer = new FileWriter("TransactionHistory.csv")) {

            // Write CSV headers
            writer.append("Date,Description,Debit,Credit,Balance\n");

            // Write data
            while (rs.next()) {
                String date = rs.getString("date");
                String description = rs.getString("description");
                double debit = rs.getDouble("debit");
                double credit = rs.getDouble("credit");
                double balance = rs.getDouble("balance");

                writer.append(String.format("%s,%s,%.2f,%.2f,%.2f\n",
                        date, description, debit, credit, balance));
            }

            System.out.println("Transaction history exported to TransactionHistory.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
