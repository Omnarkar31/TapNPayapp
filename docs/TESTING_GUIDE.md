# Testing Guide - TapNpay Login System

## ✅ Features Implemented

1. **SignUp** - Creates new user account in MySQL database
2. **Login** - Validates credentials against database
3. **Error Handling** - Shows appropriate messages

## 🧪 Test Scenarios

### Test 1: Create New Account (SignUp)

1. Run the application:
   ```powershell
   java -cp ".;lib\mysql-connector-j-8.0.33.jar" tapNpay.TapNpay
   ```

2. Click **"Sign Up"** button

3. Enter test data:
   - **Full Name**: John Doe
   - **Email**: john@test.com
   - **Password**: test123

4. Click **"Sign Up"**

5. ✅ **Expected Result**: 
   - Message: "Account created successfully!"
   - Redirected to Login page

---

### Test 2: Login with Valid Credentials

1. On Login page, enter:
   - **Email**: john@test.com
   - **Password**: test123

2. Click **"Login"** button

3. ✅ **Expected Result**:
   - Message: "Welcome John Doe!"
   - Redirected to Home page

---

### Test 3: Login with Invalid Credentials

1. On Login page, enter:
   - **Email**: wrong@test.com
   - **Password**: wrongpass

2. Click **"Login"** button

3. ✅ **Expected Result**:
   - Error message: "Account not Exist INVALID!"
   - Stays on Login page
   - Password field cleared

---

### Test 4: Login with Wrong Password

1. On Login page, enter:
   - **Email**: john@test.com (existing email)
   - **Password**: wrongpassword

2. Click **"Login"** button

3. ✅ **Expected Result**:
   - Error message: "Account not Exist INVALID!"
   - Stays on Login page

---

### Test 5: Empty Fields Validation

1. On Login page, leave fields empty or enter only one field

2. Click **"Login"** button

3. ✅ **Expected Result**:
   - Error message: "Please fill in all fields!"

---

### Test 6: Duplicate Email SignUp

1. Try to sign up with existing email:
   - **Email**: john@test.com (already exists)

2. Click **"Sign Up"**

3. ✅ **Expected Result**:
   - Error message: "Email already exists!"

---

## 🔍 Verify in Database

After testing, check the database:

```sql
USE tapnpay;
SELECT * FROM CUSTOMER_INFO;
```

You should see your test accounts listed.

---

## 🐛 Troubleshooting

### Issue: "MySQL JDBC Driver not found"
**Solution**: Run `download_mysql_connector.ps1` or manually download the JDBC driver

### Issue: "Communications link failure"
**Solution**: 
1. Start MySQL server (XAMPP/WAMP)
2. Check MySQL is running on port 3306

### Issue: "Unknown database 'tapnpay'"
**Solution**: Run the database setup script:
```sql
mysql -u root < database_setup.sql
```

Or manually create:
```sql
CREATE DATABASE tapnpay;
USE tapnpay;

CREATE TABLE CUSTOMER_INFO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    FULL_NAME VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Issue: "Account not Exist INVALID!" but account exists
**Solution**: 
1. Check email spelling (case-sensitive)
2. Check password is correct
3. Verify data in database:
   ```sql
   SELECT * FROM CUSTOMER_INFO WHERE Email = 'your@email.com';
   ```

---

## 📊 Test Data Examples

Use these for testing:

| Full Name | Email | Password |
|-----------|-------|----------|
| John Doe | john@test.com | test123 |
| Jane Smith | jane@test.com | pass456 |
| Bob Wilson | bob@test.com | secure789 |

---

## ✅ Success Checklist

- [ ] Database `tapnpay` created
- [ ] Table `CUSTOMER_INFO` created
- [ ] MySQL server is running
- [ ] Can sign up new users
- [ ] Can login with valid credentials
- [ ] Cannot login with invalid credentials
- [ ] Error messages display correctly
- [ ] Data saves to database correctly

