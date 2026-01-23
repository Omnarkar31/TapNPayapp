-- Use the database
USE tapnpay5;

-- Update Account table to include user type and vehicle number
ALTER TABLE Account 
ADD COLUMN user_type VARCHAR(20) DEFAULT 'customer',
ADD COLUMN vehicle_number VARCHAR(20);

-- Display updated table structure
DESCRIBE Account;

