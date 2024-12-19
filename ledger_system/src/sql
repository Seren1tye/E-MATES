-- Create the Ledger database if it doesn't exist
CREATE DATABASE IF NOT EXISTS Ledger;
USE Ledger;

-- Create the Users table (consolidated from multiple scripts)
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create the Transactions table (consolidated from different versions)
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

-- Create the Balance table
CREATE TABLE IF NOT EXISTS Balance (
    user_id INT PRIMARY KEY,
    current_amount DECIMAL(10, 2) NOT NULL,
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
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (loan_id) REFERENCES LoanDetails(loan_id) ON DELETE CASCADE
);

