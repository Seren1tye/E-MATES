//This program will calculate the interest(daily/monthly/annually) based on the current balance and displays it.

import java.sql.*;
import java.util.Scanner;

public class InterestPredictor {
    
    private static final String URL = "jdbc:mysql://localhost:3306/ledger_system"; // Update your DB name
    private static final String USER = "root";
    private static final String PASSWORD = "fir"; // Use your MySQL root password
    private static Scanner in = new Scanner(System.in);
    
    public static void mainInterest(int user_id) {
        int choice;
        double interestRate = interestRate(chooseBank());
        
        do {
            System.out.println("Choose interest period:");
            System.out.println("1. Daily");
            System.out.println("2. Monthly");
            System.out.println("3. Annually");
            System.out.println("4. Exit");
            System.out.print("\n> ");
            choice = in.nextInt();
            System.out.println();
            switch(choice){
                case 1:
                    System.out.printf("The daily interest is %.2f", calculateInterest(user_id, choice, interestRate));
                    System.out.println();
                    break;
                    
                case 2:
                    System.out.printf("The monthly interest is %.2f", calculateInterest(user_id, choice, interestRate));
                    System.out.println();
                    break;
                    
                case 3:
                    System.out.printf("The annual interest is %.2f", calculateInterest(user_id, choice, interestRate));
                    System.out.println();
                    break;
                    
                case 4:
                    break;
                    
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
            
        } while(choice != 4);
    }
    
    //Method to choose bank ID
    public static int chooseBank(){
        int bankID = 0;
        
        System.out.println("Choose bank:");
        System.out.println("1. RHB");
        System.out.println("2. Maybank");
        System.out.println("3. Hong Leong");
        System.out.println("4. Alliance");
        System.out.println("5. AmBank");
        System.out.println("6. Standard Chartered");
        System.out.print("\n> ");
        int bank = in.nextInt();
        System.out.println();
        
        switch (bank){
            case 1:
                bankID = 1;
                break;
                
            case 2:
                bankID = 2;
                break;
                
            case 3:
                bankID = 3;
                break;
                
            case 4:
                bankID = 4;
                break;
                
            case 5:
                bankID = 5;
                break;
                
            case 6:
                bankID = 6;
                break;
                
            default:
                break;
                
        }
        return bankID;
    }
    
    //Method to choose interest rate for bank
    public static double interestRate(int bankID){
        double rate = 0;
        
        switch (bankID){
            case 1:
                rate = 0.026;
                break;
                
            case 2:
                rate = 0.025;
                break;
                
            case 3:
                rate = 0.023;
                break;
                
            case 4:
                rate = 0.0285;
                break;
                
            case 5:
                rate = 0.0255;
                break;
                
            case 6:
                rate = 0.0265;
                break;
                
            default:
                break;
                
        }
        return rate;
    }
    
    //Method to calculate interest from the balance and interest rate (daily/monthly/annually)
    public static double calculateInterest(int user_id, int period, double interestRate){
        double interest = 0;
        
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            double balance = getBalance(user_id);
            
            switch (period){
                case 1:
                    interest = balance * interestRate / 365;
                    break;
                    
                case 2:
                    interest = balance * interestRate / 12;
                    break;
                
                case 3:
                    interest = balance * interestRate;
                    break;
                    
            }
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        return interest;
    }
    
    //Method to retrieve  current balance amount from database
    public static double getBalance(int user_id){
        double balance = 0;
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            PreparedStatement pstmt = connection.prepareStatement("SELECT current_amount FROM balance WHERE user_id = ?");
            pstmt.setInt(1, user_id);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()){
                balance = rs.getDouble("current_amount");
            }
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        return balance;
    }
}
    
    
