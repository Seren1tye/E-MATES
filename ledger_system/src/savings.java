
import java.sql.*;
import java.util.Scanner;

public class savings {

    private static final String URL = "jdbc:mysql://localhost:3306/ledger_system"; // Update your DB name
    private static final String USER = "root";
    private static final String PASSWORD = "1234"; // Use your MySQL root password

    // Display Savings Menu
    public static void showSavingsMenu(Scanner keyboard, int user_id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String userQuery = "SELECT amount FROM savings WHERE user_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(userQuery)) {
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    double balance = rs.getDouble("amount");
                    displayMenuOptions(keyboard, connection, user_id, balance);
                } else {
                    System.out.println("User ID not found or no savings yet.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Menu Options
    private static void displayMenuOptions(Scanner keyboard, Connection conn, int user_id, double balance) throws SQLException {
        System.out.println("== Welcome ==");
        System.out.println("Balance: " + balance);
        System.out.println("Savings: unavailable");
        System.out.println("Loan: unavailable");
        System.out.println("== Transaction Menu ==");
        System.out.println("1. Debit\n2. Credit\n3. Activate Savings\n4. Exit");

        int input = keyboard.nextInt();

        switch (input) {
            case 1:
                System.out.println("Debit: currently unavailable.");
                break;
            case 2:
                System.out.println("Credit: currently unavailable.");
                break;
            case 3:
                activateSavings(keyboard, conn, user_id);
                break;
            case 4:
                System.out.println("Returning to the main menu...");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    // Activate Savings
    private static void activateSavings(Scanner keyboard, Connection conn, int user_id) throws SQLException {
        System.out.print("\n== Savings Activation ==\nAre you sure you want to activate it? (Y/N): ");
        String option = keyboard.next();

        if (option.equalsIgnoreCase("Y")) {
            System.out.print("Please enter the percentage you wish to deduct from the next debit: ");
            int percentage = keyboard.nextInt();

            String insertSavingsQuery = "INSERT INTO savings (user_id, username, user_input, amount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSavingsQuery)) {
                stmt.setInt(1, user_id);
                stmt.setString(2, "Default user"); // Replace with dynamic username if available
                stmt.setString(3, "Y");
                stmt.setDouble(4, percentage);

                stmt.executeUpdate();
                System.out.println("Savings Settings added successfully!!!");
            }
        } else {
            System.out.println("Savings activation cancelled.");
        }
    }
}
