CREATE TABLE  IF NOT EXISTS users (
                       user_id SERIAL PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       pan VARCHAR(16) UNIQUE NOT NULL,
                       cvv VARCHAR(3) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
                              transaction_id SERIAL PRIMARY KEY,
                              user_id BIGINT REFERENCES users(user_id),
                              amount NUMERIC(10, 2) NOT NULL,
                              transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
