CREATE TABLE  IF NOT EXISTS users (
    user_id  serial primary key,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    pan TEXT UNIQUE NOT NULL,
    cvv TEXT NOT NULL,
    age INT NOT NULL
);
CREATE TABLE IF NOT EXISTS phone_numbers (
    phone_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    phone_number VARCHAR(15) UNIQUE NOT NULL
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

INSERT INTO users (first_name, last_name, pan, cvv, age)
SELECT 'John', 'Doe', 'lYTO1TDmje3igpH2eSVm629oSe4MC98KknCsxR8bAww=', 'lYTOBUcAHMJguiFcKFWHR7tyfg==', 25
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE pan = 'ShRCm2tqNE4nXfs0:tcBV8TTSOjIONADupAOCooVBsjHSsE2GBNw6rStfWcw='
                     );

INSERT INTO users (first_name, last_name, pan, cvv, age)
SELECT 'Jane', 'Doe', 'lYTO1TDmje3igpH2eSVm629oSe4MC98KknCsxR8bAww=/', 'lYTOBUcAHMJguiFcKFWHR7tyfg==/', 25
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE pan = 'ShRCm2tqNE4nXfs0:tcBV8TTSOjIONADupAOCooVBsjHSsE2GBNw6rStfWcw=/'
);
