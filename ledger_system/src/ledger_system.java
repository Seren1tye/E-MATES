import java.util.Scanner;

public class ledger_system {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int userId = -1; // Initialize with an invalid user ID
        int choice;

        while (true) {
            // Main menu for Sign-Up / Login / Exit
            System.out.println("=== Ledger System ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    user_actions.sign_up();
                    break;

                case 2:
                    userId = user_actions.log_in(); // Log in and retrieve userId
                    if (userId != -1) {
                        System.out.println("Login successful!\n");
                        transactionsMenu(userId); // Call transaction menu after login
                    }
                    break;

                case 3:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Sub-menu for transactions
    private static void transactionsMenu(int userId) {
        Scanner read = new Scanner(System.in);
        int option;

        do {
            // Display user balance
            System.out.println("\n=== Transactions Menu ===");
            System.out.println("Balance: " + Ledger.getBalance(userId));
            System.out.println("1. Debit");
            System.out.println("2. Credit");
            System.out.println("3. View Transaction History");
            System.out.println("4. Activate Savings");
            System.out.println("5. Credit Loan");
            System.out.println("6. Deposit Interest Predictor");
            System.out.println("7. Logout");
            System.out.print("Input: ");
            option = read.nextInt();
            read.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    Ledger.debitAmount(userId, read); // Debit transaction
                    break;
                case 2:
                    Ledger.creditAmount(userId, read); // Credit transaction
                    break;
                case 3:
                     history.displayHistory(userId); // Display transaction history
                     break;
                case 4:
                     savings.showSavingsMenu(read, userId);
                     break;
//                case 5:
//                    loan.mainloan();
//                    break;
                  case 6:
                    InterestPredictor.mainIntrest();
                    break;
                case 7:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid input, please try again.");
            }
        } while (option != 7); // Repeat until user exits
    }
}



