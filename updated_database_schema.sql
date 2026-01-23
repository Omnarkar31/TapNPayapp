-- Use the database
USE tapnpay5;

-- Update Account table to include vehicle number and user type
ALTER TABLE Account 
ADD COLUMN user_type ENUM('customer', 'driver') DEFAULT 'customer',
ADD COLUMN vehicle_number VARCHAR(20);

-- Create TRANSACTIONS table
CREATE TABLE IF NOT EXISTS TRANSACTIONS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_type ENUM('BLE', 'CARD') NOT NULL,
    receiver_vehicle_number VARCHAR(20),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'completed', 'failed') DEFAULT 'completed',
    FOREIGN KEY (sender_id) REFERENCES CUSTOMER_INFO(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES CUSTOMER_INFO(id) ON DELETE CASCADE
);

-- Display table structures
DESCRIBE Account;
DESCRIBE TRANSACTIONS;

