CREATE TABLE
    user (
        id INT PRIMARY KEY AUTO_INCREMENT,
        first_name VARCHAR(30),
        last_name VARCHAR(30),
        bank_balance FLOAT,
        mail VARCHAR(255),
        password VARCHAR(255)
    );

CREATE TABLE
    bankAccount (
        id INT PRIMARY KEY AUTO_INCREMENT,
        user_id INT NOT NULL,
        bank_name VARCHAR(50),
        iban VARCHAR(34),
        FOREIGN KEY (user_id) REFERENCES user(id)
    );

CREATE TABLE
    contact (
        id INT AUTO_INCREMENT PRIMARY KEY,
        user_1_id INT NOT NULL,
        user_2_id INT NOT NULL,
        FOREIGN KEY (user_1_Id) REFERENCES user(id),
        FOREIGN KEY (user_2_Id) REFERENCES user(id)
    );

CREATE TABLE
    transaction (
        id INT PRIMARY KEY AUTO_INCREMENT,
        credit_user_id INT NOT NULL,
        debit_user_id INT NOT NULL,
        description VARCHAR(100),
        fare FLOAT,
        date DATETIME,
        monetized_fare FLOAT,
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