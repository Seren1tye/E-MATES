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
            System.out.println("Enter your choice: ");
            System.out.print("\n>");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    user_actions.sign_up();
                    break;

                case 2:
                    String[] loginResult = user_actions.log_in(); // Retrieve userId and userName
                    userId = Integer.parseInt(loginResult[0]);
                     String userName = loginResult[1];

                    if (userId != -1) {
                        System.out.println("\n Login successful!!!\n");
                        transactionsMenu(userId, userName); // Pass the user's name to the transactions menu
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
private static void transactionsMenu(int userId, String userName) {
    Scanner read = new Scanner(System.in);
    int option;

    do {
        // Display user balance and personalized welcome message
        System.out.println("\n=== Welcome, " + userName + " ==="); // Display the user's name
        System.out.printf("Balance: %.2f\n", Ledger.getBalance(userId));
        System.out.println("Savings: aimaan's code"); // aimaan's part
        System.out.printf("Loan: %.2f\n", loan.loan(userId)); 

        System.out.println("\n=== Transactions ===");
        System.out.println("1. Debit");
        System.out.println("2. Credit");
        System.out.println("3. History");
        System.out.println("4. Savings");
        System.out.println("5. Credit Loan");
        System.out.println("6. Deposit Interest Predictor");
        System.out.println("7. Logout");
        System.out.print("\n> ");
        option = read.nextInt();
        read.nextLine(); // Consume newline

        switch (option) {
            case 1:
                if(loan.Overdue(userId)){
                    System.out.println("Please pay this month's installment to proceed with this action.");
                }else{
                    Ledger.debitAmount(userId, read); // Debit transaction
                }
                break;
            case 2:
                if(loan.Overdue(userId)){
                    System.out.println("Please pay this month's installment to proceed with this action.");
                }else{
                    Ledger.creditAmount(userId, read); // Credit transaction
                }
            case 3:
            //    history.displayHistory(userId); // Display transaction history
                break;
            case 4:
            //    savings.showSavingsMenu(read, userId);
                break;
            case 5:
                loan.loanMethod(userId);
                break;
            case 6:
            //    InterestPredictor.mainIntrest();
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


