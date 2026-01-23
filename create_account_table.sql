USE tapnpay5;

DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    phone_number VARCHAR(15),
    card_id VARCHAR(20) UNIQUE,
    balance DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER_INFO(id) ON DELETE CASCADE
);

DESCRIBE Account;

