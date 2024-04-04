CREATE TABLE  IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    pan VARCHAR(16) UNIQUE NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    age INT NOT NULL
);

CREATE TABLE IF NOT EXISTS phone_numbers (
    phone_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    phone_number VARCHAR(15) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    amount NUMERIC(10, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (user_id, first_name, last_name, pan, cvv, age)
SELECT 1, 'John', 'Doe', '1234567890123456', '123', 25
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE pan = '1234567890123456'
                     );

INSERT INTO users (user_id, first_name, last_name, pan, cvv, age)
SELECT 2, 'Jane', 'Doe', '1234567890123457', '123', 25
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE pan = '1234567890123457'
);
