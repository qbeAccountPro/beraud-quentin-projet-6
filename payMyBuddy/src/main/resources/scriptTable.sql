CREATE TABLE
    BankAccount (
        id INT PRIMARY KEY AUTO_INCREMENT,
        userId INT,
        bankName VARCHAR(50),
        iBAN VARCHAR(34),
        FOREIGN KEY (userId) REFERENCES User(id)
    );

CREATE TABLE
    Contact (
        user_1_Id INT,
        user_2_Id INT,
        PRIMARY KEY (user_1_Id, user_2_Id),
        FOREIGN KEY (user_1_Id) REFERENCES User(id),
        FOREIGN KEY (user_2_Id) REFERENCES User(id)
    );

CREATE TABLE
    Identifier (
        id INT PRIMARY KEY AUTO_INCREMENT,
        userId INT,
        mail VARCHAR(255),
        password VARCHAR(50),
        FOREIGN KEY (userId) REFERENCES User(id)
    );

CREATE TABLE
    Transaction (
        id INT PRIMARY KEY AUTO_INCREMENT,
        creditUserId INT,
        debitUserId INT,
        description VARCHAR(100),
        fare FLOAT,
        date DATE,
        FOREIGN KEY (creditUserId) REFERENCES Utilisateur(id),
        FOREIGN KEY (debitUserId) REFERENCES Utilisateur(id)
    );

CREATE TABLE
    User (
        id INT PRIMARY KEY AUTO_INCREMENT,
        firstName VARCHAR(30),
        lastName VARCHAR(30),
        bankBalance FLOAT
    );