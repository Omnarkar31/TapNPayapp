-- Create database
CREATE DATABASE IF NOT EXISTS tapnpay5;

-- Use the database
USE tapnpay5;

-- Create CUSTOMER_INFO table (for user login and signup)
CREATE TABLE IF NOT EXISTS CUSTOMER_INFO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    FULL_NAME VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Drop Account table if exists (to recreate with correct structure)
DROP TABLE IF EXISTS Account;

-- Create Account table (for phone, card ID, and balance)
CREATE TABLE Account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    phone_number VARCHAR(15),
    card_id VARCHAR(20) UNIQUE,
    balance DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER_INFO(id) ON DELETE CASCADE
);

-- Display table structures
DESCRIBE CUSTOMER_INFO;
DESCRIBE Account;

-- Show all tables
SHOW TABLES;

