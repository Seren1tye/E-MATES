-- Disable safe updates to allow unrestricted updates and deletes
SET SQL_SAFE_UPDATES = 0;

-- Create the Ledger database if it doesn't exist
CREATE DATABASE IF NOT EXISTS ledger_system;
USE ledger_system;

-- Create the Users table
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create the Balance table
CREATE TABLE IF NOT EXISTS Balance (
    user_id INT PRIMARY KEY,
    current_amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Create the Transactions table
CREATE TABLE IF NOT EXISTS Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255) NOT NULL,
    debit DECIMAL(10, 2) DEFAULT 0.00,
    credit DECIMAL(10, 2) DEFAULT 0.00,
    balance DECIMAL(10, 2) DEFAULT 0.00,
    transaction_type ENUM('debit', 'credit') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Create the Savings table
CREATE TABLE IF NOT EXISTS Savings (
    savings_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Create the BankDetails table
CREATE TABLE IF NOT EXISTS BankDetails (
    bank_id INT AUTO_INCREMENT PRIMARY KEY,
    bank_name VARCHAR(255) NOT NULL,
    interest_rate DECIMAL(5, 4) NOT NULL
);

-- Insert sample data into BankDetails
INSERT INTO BankDetails (bank_id, bank_name, interest_rate)
VALUES
    (1, 'RHB', 0.026), 
    (2, 'Maybank', 0.025),  
    (3, 'Hong Leong', 0.023),  
    (4, 'Alliance', 0.0285),   
    (5, 'AmBank', 0.0255),   
    (6, 'Standard Chartered', 0.0265);

-- Create the LoanDetails table
CREATE TABLE IF NOT EXISTS LoanDetails (
    user_id INT NOT NULL,
    loan_id INT,
    principal DECIMAL(65, 2) NOT NULL,
    interest_rate INT NOT NULL,
    repayment_period INT NOT NULL,
    monthly_repay DECIMAL(65, 2) NOT NULL,
    loan_balance DECIMAL(65, 2) NOT NULL,
    status VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Create the Repay table
CREATE TABLE IF NOT EXISTS Repay (
    user_id INT NOT NULL,
    loan_id INT NOT NULL,
    repay_id INT,
    repayment DECIMAL(65, 2) NOT NULL,
    loan_balance DECIMAL(65, 2) NOT NULL,
    payment_date DATE NOT NULL,
    due_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Stored Procedure to Add a Transaction
DELIMITER $$

CREATE PROCEDURE AddTransaction(
    IN p_user_id INT,
    IN p_description VARCHAR(255),
    IN p_debit DECIMAL(10, 2),
    IN p_credit DECIMAL(10, 2),
    IN p_transaction_type ENUM('debit', 'credit')
)
BEGIN
    DECLARE new_balance DECIMAL(10, 2);

    -- Get the current balance
    SELECT current_amount INTO new_balance
    FROM Balance
    WHERE user_id = p_user_id;

    -- Update the balance based on transaction type
    IF p_transaction_type = 'debit' THEN
        SET new_balance = new_balance - p_debit;
    ELSE
        SET new_balance = new_balance + p_credit;
    END IF;

    -- Insert the transaction with the updated balance
    INSERT INTO Transactions (user_id, description, debit, credit, balance, transaction_type)
    VALUES (p_user_id, p_description, p_debit, p_credit, new_balance, p_transaction_type);

    -- Update the user's balance in the Balance table
    UPDATE Balance
    SET current_amount = new_balance
    WHERE user_id = p_user_id;
END$$

DELIMITER ;

-- Trigger to Update Balance After a Transaction
DELIMITER $$

CREATE TRIGGER update_balance_after_transaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    -- Update the user's balance in the Balance table
    IF NEW.transaction_type = 'debit' THEN
        UPDATE Balance
        SET current_amount = current_amount - NEW.debit
        WHERE user_id = NEW.user_id;
    ELSE
        UPDATE Balance
        SET current_amount = current_amount + NEW.credit
        WHERE user_id = NEW.user_id;
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER add_balance_after_user_creation
AFTER INSERT ON Users
FOR EACH ROW
BEGIN
    -- Insert a new record into the Balance table with a default amount of 0
    INSERT INTO Balance (user_id, current_amount)
    VALUES (NEW.user_id, 0.00);
END$$

DELIMITER ;

