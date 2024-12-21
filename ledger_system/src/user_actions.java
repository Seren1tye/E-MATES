import java.sql.*;
import java.util.Scanner;

public class user_actions {
    
 public static void sign_up() {
    try {
        Connection connection = DB.Connect();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n== Please fill in the form ==");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Validate name (alphanumeric check)
        if (!name.matches("[a-zA-Z0-9\\s]+")) {
            System.out.println("Error: Name must be alphanumeric and cannot contain special characters.");
            return;
        }

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            System.out.println("Error: Invalid email format.");
            return;
        }

        // Check if the email already exists in the database
        String emailCheckQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
        PreparedStatement emailCheckStatement = connection.prepareStatement(emailCheckQuery);
        emailCheckStatement.setString(1, email);
        ResultSet resultSet = emailCheckStatement.executeQuery();
        resultSet.next(); // Move the cursor to the first row

        if (resultSet.getInt(1) > 0) { // Check if the count is greater than 0
            System.out.println("Error: This email is already registered.");
            return;
        }

        System.out.print("Enter your password: ");
        String pass = scanner.nextLine();

        // Validate password complexity
        if (pass.length() < 8) {
            System.out.println("Error: Password must be at least 8 characters long.");
            return;
        }

        System.out.print("Confirm your password: ");
        String confirmPass = scanner.nextLine();

        // Check if passwords match
        if (!pass.equals(confirmPass)) {
            System.out.println("Error: Passwords do not match!");
            return;
        }

        // SQL to insert data
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setString(2, email);
        statement.setString(3, pass); // For now, plaintext (later secure this with hashing)

        int rowsInserted = statement.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Registration successful!!! \n");
        }
    } catch (SQLException e) {
        System.out.println("Database Error: " + e.getMessage());
    }
}

public static String[] log_in() {
    int userId = -1; // Invalid by default
    String userName = null; // To hold the user's name
    try {
        Connection connection = DB.Connect();
        Scanner scanner = new Scanner(System.in);
        System.out.println("== Please enter your email and password ==");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String pass = scanner.nextLine();

        String sql = "SELECT user_id, name FROM users WHERE email = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, email);
        statement.setString(2, pass);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            userId = resultSet.getInt("user_id");
            userName = resultSet.getString("name");
        } else {
            System.out.println("Invalid email or password.");
        }
    } catch (SQLException e) {
        System.out.println("Error during login.");
        e.printStackTrace();
    }
    return new String[]{String.valueOf(userId), userName}; // Return both userId and userName
}


        
        }
    
    

