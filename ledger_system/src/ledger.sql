CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);


//

CREATE TABLE IF NOT EXISTS Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY, -- Unique ID for each transaction
    user_id INT NOT NULL,                          -- ID of the user performing the transaction
    amount DECIMAL(10, 2) NOT NULL,                -- Transaction amount (positive for debit, negative for credit)
    description VARCHAR(255) NOT NULL,            -- Description of the transaction
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP, -- Date and time of the transaction
    transaction_type ENUM('debit', 'credit') NOT NULL, -- Type of transaction ('debit' or 'credit')
    Add foreign key (user_id) references users (user_id)
);

//

CREATE TABLE bankdetails(
	bank_id INT PRIMARY KEY AUTO_INCREMENT,
    bank_name VARCHAR (255),
    interest_rate DECIMAL (5,4)
    );
    
INSERT INTO bankDetails  
	VALUES (1, "RHB", 0.026), 
		     (2, "Maybank", 0.025),  
           (3, "Hong Leong", 0.023),  
           (4, "Alliance", 0.0285),   
           (5, "AmBank", 0.0255),   
           (6, "Standard Chartered", 0.0265);

//

CREATE TABLE TransactionsHistory (
    user_id int,
    date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    debit DECIMAL(10, 2),
    credit DECIMAL(10, 2),
    balance DECIMAL(10, 2),
    Add foreign key (user_id) references users (user_id)
);
