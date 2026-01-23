# Quick Start Guide - TapNpay

## ✅ What's Already Done

1. ✅ MySQL JDBC Driver downloaded to `lib\` folder
2. ✅ SignUp form connected to MySQL database
3. ✅ Database: `tapnpay`, Username: `root`, Password: `` (empty)

## 🚀 Steps to Run

### 1. Setup MySQL Database

Run this SQL in MySQL/phpMyAdmin:

```sql
CREATE DATABASE IF NOT EXISTS tapnpay;
USE tapnpay;

CREATE TABLE IF NOT EXISTS CUSTOMER_INFO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    FULL_NAME VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Or run: `mysql -u root < database_setup.sql`

### 2. Run the Application

**Option A - Command Line:**
```powershell
java -cp ".;lib\mysql-connector-j-8.0.33.jar" tapNpay.TapNpay
```

**Option B - VS Code:**
1. Open project in VS Code
2. Press **F5** or click "Run > Start Debugging"
3. Select "Run TapNpay" configuration

### 3. Test Signup

1. Click "Sign Up" button on login screen
2. Enter:
   - Full Name: John Doe
   - Email: john@example.com
   - Password: password123
3. Click "Sign Up"
4. You should see: "Account created successfully!"

### 4. Verify in Database

```sql
USE tapnpay;
SELECT * FROM CUSTOMER_INFO;
```

## 🛠️ Compile (if needed)

```powershell
javac -cp ".;lib\mysql-connector-j-8.0.33.jar" -d . *.java
```

## ⚠️ Troubleshooting

**MySQL not running?**
- Start XAMPP/WAMP MySQL service
- Or start MySQL service: `net start MySQL`

**Database doesn't exist?**
- Run `database_setup.sql` script

**Can't connect to MySQL?**
- Check MySQL is running on port 3306
- Verify root password in `SignUp.java` (currently empty)

## 📁 Project Structure

```
TapNpay application/
├── lib/
│   └── mysql-connector-j-8.0.33.jar  ← JDBC Driver
├── tapNpay/
│   ├── TapNpay.class
│   ├── Login.class
│   ├── SignUp.class                  ← Has MySQL connection
│   └── Home.class
├── TapNpay.java
├── Login.java
├── SignUp.java                        ← MySQL code here
├── Home.java
├── database_setup.sql                 ← Run this in MySQL
└── QUICKSTART.md                      ← You are here
```

