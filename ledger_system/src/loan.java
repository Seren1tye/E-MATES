


import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class loan{
    
    
    static Scanner sc = new Scanner(System.in);
      
    public static void loanMethod (int user_id){
        CreditLoan:{
            while(true){
                
                double loan = loan(user_id);
                double balanceMain = balanceMain(user_id);
                String status= status(user_id);
                int loan_id= loanId(user_id);
                int repay_id = repayId(user_id, loan_id);



                System.out.println("""

                                1.Apply
                                2.Repay
                                3.Exit
                                """);
                 int menu = sc.nextInt();

                switch(menu){
                    case 1:
                        if(loan_id==0 || status.equals("repaid")){
                            apply(user_id,status, loan_id);
                        }else{
                            System.out.println("Complete your current loan repayment before applying for a new one.");
                        }
                        break;

                    case 2:
                        if(loan_id==0 || status.equals("repaid")){
                            System.out.println("No loan applied.");
                        }else{
                            repay(user_id,loan_id,repay_id,loan);
                            
                        }
                        
                        break;

                    case 3:
                        break CreditLoan;
                    default:
                        System.out.println("error");
                }
            }
        }
    }
    
    public static double balanceMain (int user_id){
        double balance=0;
        
        try {
            Connection conn = DB.Connect();
            String sql = "select current_amount from balance where user_id = ?; ";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1,user_id);
            
            ResultSet rs = statement.executeQuery();
            
            if(rs.next()){
                balance = rs.getDouble("current_amount");
            }
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        return balance;
    }
    
    public static boolean Overdue(int user_id){
        boolean Overdue=false;
        try {
            Connection conn = DB.Connect();
            
            double loan = loan(user_id);
            Calendar calendar = Calendar.getInstance();
            Date created_at=new Date(calendar.getTimeInMillis());
            Date dueDate=new Date(calendar.getTimeInMillis());
            Date currentDate = new Date(calendar.getTimeInMillis());
            double M=0,balance=0;
            int totalPeriod=0;
            
            
            String sql1 = "select created_at, monthly_repay, repayment_period from loandetails where user_id=? and loan_id = (select MAX(loan_id) from loandetails where user_id= ?); ";
            PreparedStatement statement1 = conn.prepareStatement(sql1);
            statement1.setInt(1, user_id);
            statement1.setInt(2, user_id);

            ResultSet rs1= statement1.executeQuery();
            
            if(rs1.next()){
                M = rs1.getDouble("monthly_repay");
                created_at = rs1.getDate("created_at");
                totalPeriod = rs1.getInt("repayment_period");
                 
            }
            
            String sql2 = "select loan_balance, due_date from repay where user_id = ? and loan_id = (select MAX(loan_id) from repay where user_id= ?) and repay_id = (select MAX(repay_id) from repay where user_id= ? and loan_id = (select MAX(loan_id) from repay where user_id= ?))";
            PreparedStatement statement2 = conn.prepareStatement(sql2);
            statement2.setInt(1, user_id);
            statement2.setInt(2, user_id);
            statement2.setInt(3, user_id);
            statement2.setInt(4, user_id);
            
            ResultSet rs2= statement2.executeQuery();
            
            if(rs2.next()){
                balance  = rs2.getDouble("loan_balance");
                dueDate = rs2.getDate("due_date");
            }
            
            calendar.setTime(created_at);
            int year1 = calendar.get(Calendar.YEAR);
            int month1 = calendar.get(Calendar.MONTH);
            
            calendar.setTime(dueDate);
            int year2 = calendar.get(Calendar.YEAR);
            int month2 = calendar.get(Calendar.MONTH);
            
            int diff = (year2-year1)*12 + (month2-month1);
            int period = totalPeriod-diff;
             
            if(balance>(M*period)&& currentDate.compareTo(dueDate)>0){
                Overdue=true;
            }
            
            
            
        } catch (SQLException e){
            e.printStackTrace();
        }
            return Overdue;
    }
    
    public static double loan(int user_id){
        double balance=0.00;
        try {
            Connection conn = DB.Connect();
            String sql= "select loan_balance from loandetails where user_id=? and loan_id = (select MAX(loan_id) from loandetails where user_id= ?);";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, user_id);
            
            ResultSet rs= statement.executeQuery();
            
            if(rs.next()){
                balance = rs.getDouble("loan_balance");
            }
         
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        return balance;
    }
    
    public static String status(int user_id){
        
        String status="repaid";
        try {
            Connection conn = DB.Connect();
            String sql= "select status from loandetails where user_id=? and loan_id = (select MAX(loan_id) from loandetails where user_id= ?);";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, user_id);
            
            ResultSet rs= statement.executeQuery();
            
            if(rs.next()){
                status = rs.getString("status");
            }
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        return status;
    }
    
    public static int loanId(int user_id){
        int y=0;
        try {
            Connection conn = DB.Connect();
            String sql= "select MAX(loan_id) as MaxLoanId from loandetails where user_id=?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, user_id);
            
            ResultSet rs= statement.executeQuery();
            
            if(rs.next()){
                y = rs.getInt("MaxLoanId");
                
            }
            
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        return y;
    }
    
    public static void apply(int user_id, String status, int loan_id){
        try {
            Connection conn = DB.Connect();
            double balanceMain=0;
            status = "active";
            double principal,interest,period,loan,M;
            
            Date created_at;
            System.out.print("Enter principal: ");
            principal = sc.nextDouble();
            
            
            
            System.out.print("Enter interest: ");
            interest = sc.nextDouble();
            double interestCopy= interest;
            interest = interest/(12.0*100.0);
            
        
            System.out.print("Enter period: ");
            period = sc.nextDouble();
        
            M = ( principal * interest ) / ( 1 - (Math.pow( 1+interest, -period ) ) );
            M = Math.round(M * 100.0) / 100.0;
        
            loan = M*period;
            loan = Math.round(loan * 100.0) / 100.0;
            
            //prepare query
            String sql = "INSERT INTO loandetails (user_id,loan_id,principal,interest_rate,repayment_period,monthly_repay,loan_balance,status,created_at) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            
            
            //get date for when user apply for loan
            Calendar calendar = Calendar.getInstance();
            created_at = new Date(calendar.getTimeInMillis());
            
            
            
            //set parameter
            statement.setInt(1,user_id);
            statement.setInt(2, (loan_id+1));
            statement.setDouble(3, principal);
            statement.setDouble(4, interestCopy);
            statement.setDouble(5, period);
            statement.setDouble(6, M);
            statement.setDouble(7, loan);
            statement.setString(8, status);
            statement.setDate(9, created_at);
            
            
            //execute
            statement.executeUpdate();

            System.out.println("Loan application completed!");
            System.out.println("Applied on: "+created_at);
            System.out.println("Total Repayment: "+loan);
            System.out.println("Monthly Repayment: "+M);
            
            balanceMain = balanceMain(user_id);
            balanceMain+= principal;
            balanceMain= Math.round(balanceMain*100.0)/100.0;
            
            
            String sql2 = "update balance set current_amount = ? where user_id = ?;";
            PreparedStatement statement2 = conn.prepareStatement(sql2);
            statement2.setDouble(1,balanceMain);
            statement2.setInt(2, user_id);
            
            statement2.executeUpdate();
            
            
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public static int repayId(int user_id, int loan_id){
        int repay_id=0;
        double balance=1;
        try {
            Connection conn = DB.Connect();
            String sql= "select MAX(repay_id) AS MaxRepayId from repay where user_id=? and loan_id = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, loan_id);
            
            ResultSet rs= statement.executeQuery();
            
            if(rs.next()){
                repay_id = rs.getInt("MaxRepayId"); 
            }
            
            String sql2 = "select loan_balance from repay where user_id = ? and loan_id = ?-1; ";
            PreparedStatement statement2 = conn.prepareStatement(sql2);
            statement2.setInt(1, user_id);
            statement2.setInt(2, loan_id);
            
            ResultSet rs2= statement2.executeQuery();
            
            if(rs2.next()){
                balance = rs2.getDouble("loan_balance"); 
            }
            
            if(balance==0){
                repay_id=0;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return repay_id;
    }
    
    public static void repay(int user_id,int loan_id, int repay_id, double loan){
        
        try {
            Connection conn = DB.Connect();
            double balanceMain= balanceMain(user_id);
            int installment;
            Calendar calendar = Calendar.getInstance();
            Date created_at=new Date(calendar.getTimeInMillis()),payment_date= new Date(calendar.getTimeInMillis()),dueDate=new Date(calendar.getTimeInMillis());
            double M=0,repay;
            int period=0;
            
            String sql1 = "select created_at, monthly_repay, repayment_period from loandetails where user_id=? and loan_id = (select MAX(loan_id) from loandetails where user_id= ?); ";
            PreparedStatement statement1 = conn.prepareStatement(sql1);
            statement1.setInt(1, user_id);
            statement1.setInt(2, user_id);

            ResultSet rs= statement1.executeQuery();
            
            if(rs.next()){
                M = rs.getDouble("monthly_repay");
                period = rs.getInt("repayment_period");
                created_at = rs.getDate("created_at");
                calendar.setTime(created_at); 
            }
            
            double dueCalc = M*period - loan;
            dueCalc = Math.round(dueCalc*100.0)/100.0;
            dueCalc/=M;
            installment = (int) Math.floor(dueCalc);
            
            
            if (repay_id==0){
                
             
                calendar.add(Calendar.MONTH, 1);
                dueDate = new Date(calendar.getTimeInMillis());
                
                
            }else{
                calendar.add(Calendar.MONTH, installment+1);
                dueDate = new Date(calendar.getTimeInMillis());
            }
            
            double InstallmentDue= M*period - loan;
            InstallmentDue%= M;
            InstallmentDue = M- InstallmentDue;
            InstallmentDue = Math.round(InstallmentDue*100.0)/100.0;
            
            
            System.out.println("Outstanding Loan Amount: "+loan);
            System.out.println("Next Installment : "+InstallmentDue+ " due on "+dueDate);
            System.out.println("=================================================");
            System.out.print("Enter repayment amount: ");
            repay = sc.nextDouble();
            
            if(repay>balanceMain){
                System.out.println("Insuficcient funds.");
            }else if (repay>loan){
                System.out.println("You’re paying more than required for your loan.");
            }else{
                
                balanceMain-=repay;
                loan-= repay;
                loan = Math.round(loan*100.0)/100.0;
                System.out.println("Remaining Loan Balance: "+loan);


                dueCalc = M*period - loan;
                dueCalc = Math.round(dueCalc*100.0)/100.0;
                dueCalc/=M;
                installment = (int) Math.floor(dueCalc);


                if(repay>=InstallmentDue){
                    calendar.setTime(created_at); 
                    calendar.add(Calendar.MONTH, installment+1);
                    dueDate = new Date(calendar.getTimeInMillis());
                }

                InstallmentDue= M*period - loan;
                InstallmentDue%= M;
                InstallmentDue = M- InstallmentDue;
                InstallmentDue = Math.round(InstallmentDue*100.0)/100.0;



                if(loan!=0){
                    System.out.println("Next Installment: "+InstallmentDue+" due "+dueDate);
                }else{
                    System.out.println("All loans have been fully settled!");
                    }


                    String sql2 = "Insert into repay values (?,?,?,?,?,?,?);";
                    PreparedStatement statement2 = conn.prepareStatement(sql2);

                    statement2.setInt(1,user_id);
                    statement2.setInt(2, loan_id);
                    statement2.setInt(3, (repay_id+1));
                    statement2.setDouble(4, repay);
                    statement2.setDouble(5, loan);
                    statement2.setDate(6, payment_date);
                    statement2.setDate(7, dueDate);

                    statement2.executeUpdate();


                    String sql3 = "update loandetails set loan_balance = ? where user_id = ? and loan_id = ?; ";
                    PreparedStatement statement3 = conn.prepareStatement(sql3);

                    statement3.setDouble(1,loan);
                    statement3.setInt(2,user_id);
                    statement3.setInt(3,loan_id);

                    statement3.executeUpdate();

                    if (loan==0){
                        String sql4 = "update loandetails set status = \"repaid\" where user_id = ? and loan_id = ?; ";
                        PreparedStatement statement4 = conn.prepareStatement(sql4);

                        statement4.setInt(1,user_id);
                        statement4.setInt(2,loan_id);

                        statement4.executeUpdate();
                    }
                    
                    String sql6 = "insert into transactions (user_id, description, credit, balance, transaction_type) values (?,?,?,?,?)";
                    PreparedStatement statement6 = conn.prepareStatement(sql6);
                    
                    statement6.setInt(1,user_id);
                    statement6.setString(2, "Loan repayment");
                    statement6.setDouble(3, repay);
                    statement6.setDouble(4, balanceMain);
                    statement6.setString(5, "credit");

                    statement6.executeUpdate();
                    
                    
                    String sql5 = "update balance set current_amount = ? where user_id = ?;";
                    PreparedStatement statement5 = conn.prepareStatement(sql5);
                    
                    statement5.setDouble(1,balanceMain);
                    statement5.setInt(2, user_id);
                    
                    statement5.executeUpdate();
                    
                    
            }
            
            
            
        }catch (SQLException e){
            e.printStackTrace();
        }
        
    }
}
