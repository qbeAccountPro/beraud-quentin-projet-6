INSERT INTO
    BankAccount (userId, bankName, iBAN)
VALUES (
        1,
        'Caisse d\'épargne',
        '123456789'
    ), (
        2,
        'Crédit agricolt',
        '987654321'
    ), (
        3,
        'Banque postale',
        '555555555'
    );

INSERT INTO
    Contact (user_1_Id, user_2_Id)
VALUES (1, 2), (1, 3), (2, 3);

INSERT INTO
    Transaction (
        creditUserId,
        debitUserId,
        description,
        fare,
        date
    )
VALUES (
        1,
        2,
        'Vacances plages',
        100.50,
        '2023-09-18'
    ), (
        2,
        3,
        'Projet soudure',
        50.75,
        '2023-09-19'
    ), (
        3,
        1,
        'Réparation vélo',
        75.20,
        '2023-09-20'
    );

/* TODO : check password format or creation  */

INSERT INTO
    User (
        firstName,
        lastName,
        bankBalance,
        mail,
        password
    )
VALUES (
        'John',
        'Doe',
        1000.75,
        'john.doe@gmail.com',
        ''
    ), (
        'Jane',
        'Smith',
        500.25,
        'jane.smith@gmail.com',
        ''
    ), (
        'Alice',
        'Johnson',
        750.90,
        'alice.johnson@gmail.com',
        ''
    );