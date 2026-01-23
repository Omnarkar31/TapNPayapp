-- Use the database
USE tapnpay5;

-- Create TRANSACTIONS table
CREATE TABLE IF NOT EXISTS TRANSACTIONS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_type VARCHAR(20) DEFAULT 'TAP_PAY',
    receiver_name VARCHAR(100),
    receiver_vehicle_number VARCHAR(20),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'completed',
    FOREIGN KEY (sender_id) REFERENCES CUSTOMER_INFO(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES CUSTOMER_INFO(id) ON DELETE CASCADE
);

-- Display table structure
DESCRIBE TRANSACTIONS;

