CREATE TABLE
    user (
        id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
        first_name VARCHAR(30) NOT NULL,
        last_name VARCHAR(30) NOT NULL,
        bank_balance FLOAT NOT NULL,
        mail VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL
    );

CREATE TABLE
    bankAccount (
        id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
        user_id INT NOT NULL,
        bank_name VARCHAR(50),
        iban VARCHAR(34) NOT NULL,
        FOREIGN KEY (user_id) REFERENCES user(id)
    );

CREATE TABLE
    contact (
        id INT AUTO_INCREMENT PRIMARY KEY  NOT NULL,
        user_1_id INT NOT NULL,
        user_2_id INT NOT NULL,
        FOREIGN KEY (user_1_Id) REFERENCES user(id),
        FOREIGN KEY (user_2_Id) REFERENCES user(id)
    );

CREATE TABLE
    transaction (
        id INT PRIMARY KEY AUTO_INCREMENT  NOT NULL,
        credit_user_id INT NOT NULL,
        debit_user_id INT NOT NULL,
        description VARCHAR(100) NOT NULL,
        fare FLOAT NOT NULL,
        date DATETIME NOT NULL,
        monetized_fare FLOAT NOT NULL,
        FOREIGN KEY (credit_user_id) REFERENCES user(id),
        FOREIGN KEY (debit_user_id) REFERENCES user(id)
    );

CREATE TABLE
    persistent_logins (
        username VARCHAR(255) NOT NULL,
        series VARCHAR(64) KEY,
        token VARCHAR(64) NOT NULL,
        last_used TIMESTAMP
    );