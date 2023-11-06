CREATE TABLE
    User (
        id INT PRIMARY KEY AUTO_INCREMENT,
        first_name VARCHAR(30),
        last_name VARCHAR(30),
        bank_balance FLOAT,
        mail VARCHAR(255),
        password VARCHAR(255)
    );

CREATE TABLE
    BankAccount (
        id INT PRIMARY KEY AUTO_INCREMENT,
        user_id INT,
        bank_name VARCHAR(50),
        iban VARCHAR(34),
        FOREIGN KEY (userId) REFERENCES User(id)
    );

CREATE TABLE
    Contact (
        id INT AUTO_INCREMENT PRIMARY KEY,
        user_1_id INT NOT NULL,
        user_2_id INT NOT NULL,
        FOREIGN KEY (user_1_Id) REFERENCES User(id),
        FOREIGN KEY (user_2_Id) REFERENCES User(id)
    );


CREATE TABLE
    Transaction (
        id INT PRIMARY KEY AUTO_INCREMENT,
        credit_user_id INT,
        debit_user_id INT,
        description VARCHAR(100),
        fare FLOAT,
        date DATE,
        FOREIGN KEY (creditUserId) REFERENCES User(id),
        FOREIGN KEY (debitUserId) REFERENCES User(id)
    );