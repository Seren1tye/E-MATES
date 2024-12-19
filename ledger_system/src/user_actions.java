import java.sql.*;
import java.util.Scanner;

public class user_actions {
    
        public static void sign_up(){
            
            try{
                Connection connection = DB.Connect();
                          Scanner scanner = new Scanner(System.in);
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
                System.out.println("Registration successful!");
            }
                }
            catch(SQLException e){
                System.out.println(e);
            }
            
            
        }
public static int log_in() {
    int userId = -1; // Invalid by default
    try {
        Connection connection = DB.Connect();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String pass = scanner.nextLine();

        String sql = "SELECT user_id, name FROM users WHERE email = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, email);
        statement.setString(2, pass);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            userId = resultSet.getInt("user_id");
            System.out.println("Welcome, " + resultSet.getString("name") + "!");
        } else {
            System.out.println("Invalid email or password.");
        }
    } catch (SQLException e) {
        System.out.println("Error during login.");
        e.printStackTrace();
    }
    return userId; // Return the logged-in user ID
}

        
        }
    
    

