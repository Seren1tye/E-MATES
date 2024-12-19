import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class history {

    // Method to display the transaction history for a given user
    public static void displayHistory(int userId) {
        try (Connection connection = DB.Connect();
             Statement stmt = connection.createStatement()) {

            // Adjusting the query to filter by user_id to show only the current user's history
            String query = "SELECT date, description, debit, credit, balance FROM TransactionsHistory WHERE user_id = " + userId;
            ResultSet rs = stmt.executeQuery(query);

            // Table headers for the output
            System.out.printf("%-20s %-30s %-10s %-10s %-10s%n", "Date", "Description", "Debit", "Credit", "Balance");
            System.out.println("-------------------------------------------------------------");

            // Loop through the results and display each record
            while (rs.next()) {
                String date = rs.getString("date");
                String description = rs.getString("description");
                double debit = rs.getDouble("debit");
                double credit = rs.getDouble("credit");
                double balance = rs.getDouble("balance");

                // Printing each transaction record in formatted style
                System.out.printf("%-20s %-30s %-10.2f %-10.2f %-10.2f%n", date, description, debit, credit, balance);
            }
        } catch (Exception e) {
            e.printStackTrace(); // In case of any exception, print the stack trace for debugging
        }
    }

    public static void main(String[] args) {
        // This would ideally be replaced by a dynamically set userId after login
        // Sample test: Display transaction history for user with ID = 6
        int userId = 6; // Example user ID - In actual usage, this would come from the login system
        displayHistory(userId);  // Call method to display user's transaction history
    }
}
