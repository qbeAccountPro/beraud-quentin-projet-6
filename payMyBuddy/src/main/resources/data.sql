INSERT INTO
    user (
        first_name,
        last_name,
        bank_balance,
        mail,
        password
    )
VALUES (
        'John',
        'Doe',
        1000.75,
        'john.doe@gmail.com',
        '$2a$10$67CkGABIjJIYPlXoBxPCrOOWKJSuHFZ9UDXlYDTjE2Zatg.9u2ShS'
    ), (
        'Jane',
        'Smith',
        500.25,
        'jane.smith@gmail.com',
        '$2a$10$lo5wgvUeoaJgM17DFJWjbuZZEPS4TmSyeJmvbPA8dxv4hqemsP0Z2'
    ), (
        'Alice',
        'Johnson',
        750.90,
        'alice.johnson@gmail.com',
        '$2a$10$J3P0bYDyg0iWCHZOadc2T.U4W6Db9/sSsvVTART1Wo2r1cTuIyp3C'
    );

INSERT INTO
    bankAccount (user_id, bank_name, iban)
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
    Contact (user_1_id, user_2_id)
VALUES (1, 2), (1, 3), (2, 3);

INSERT INTO
    transaction (
        credit_user_id,
        debit_user_id,
        description,
        fare,
        date,
        monetized_fare
    )
VALUES (
        1,
        2,
        'Vacances plages',
        100.50,
        '2023-09-18 10:00:00',
        0.05
    ), (
        2,
        3,
        'Projet soudure',
        50.75,
        '2023-09-19 11:00:00',
        0.03
    ), (
        3,
        1,
        'Réparation vélo',
        75.20,
        '2023-09-20 12:00:00',
        0.04
    );