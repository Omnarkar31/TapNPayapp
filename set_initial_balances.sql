-- Use the database
USE tapnpay5;

-- Set balance to 500 for all customer accounts
UPDATE Account SET balance = 500.00 WHERE user_type = 'customer';

-- Set balance to 0 for all driver accounts
UPDATE Account SET balance = 0.00 WHERE user_type = 'driver';

-- Set default balance for accounts without user_type (treat as customers)
UPDATE Account SET balance = 500.00 WHERE user_type IS NULL OR user_type = '';

-- Show current balances
SELECT 
    c.FULL_NAME, 
    a.user_type, 
    a.balance 
FROM Account a 
JOIN CUSTOMER_INFO c ON a.customer_id = c.id;

