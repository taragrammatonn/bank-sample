CREATE TABLE  IF NOT EXISTS users (
                                      user_id SERIAL PRIMARY KEY,
                                      first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    pan VARCHAR(16) UNIQUE NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    age INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS addresses (
                                         address_id SERIAL PRIMARY KEY,
                                         user_id INT REFERENCES users(user_id),
    street VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(10),
    country VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS transactions (
                                            transaction_id SERIAL PRIMARY KEY,
                                            user_id BIGINT REFERENCES users(user_id),
    amount NUMERIC(10, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO users values (1, 'John', 'Doe', '1234567890123456', '123', 25);
INSERT INTO users values (2, 'Jane', 'Doe', '1234567890123457', '123', 25);

--Addresses

INSERT INTO addresses values (1, 1, '123 Main St', 'Anytown', 'NY', '12345', 'USA');
INSERT INTO addresses values (2, 2, '124 Main St', 'Anytown', 'NY', '12345', 'USA');


--Transactions

INSERT INTO transactions values (1, 1, 100.00, '2020-01-01 00:00:00');
INSERT INTO transactions values (2, 2, 200.00, '2020-01-01 00:00:00');