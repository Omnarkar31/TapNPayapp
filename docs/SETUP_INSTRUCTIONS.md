# TapNpay Application Setup

## Prerequisites

1. **Java JDK** (version 8 or higher)
2. **MySQL Server** (version 5.7 or higher)
3. **MySQL JDBC Driver** (mysql-connector-java)

## Database Setup

1. **Start MySQL Server** (XAMPP, WAMP, or standalone MySQL)

2. **Create Database and Table**:
   - Open MySQL command line or phpMyAdmin
   - Run the SQL script:
   ```bash
   mysql -u root -p < src/main/resources/db/complete_database_setup.sql
   ```
   
   Then run the update script:
   ```bash
   mysql -u root -p < src/main/resources/db/updated_database_schema.sql
   ```
   
   Or manually execute in MySQL:
   ```sql
   CREATE DATABASE IF NOT EXISTS tapnpay5;
   USE tapnpay5;
   
   CREATE TABLE IF NOT EXISTS CUSTOMER_INFO (
       id INT AUTO_INCREMENT PRIMARY KEY,
       FULL_NAME VARCHAR(100) NOT NULL,
       Email VARCHAR(100) NOT NULL UNIQUE,
       Password VARCHAR(255) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

## MySQL JDBC Driver Setup

### Option 1: Download Manually
1. Download MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/
2. Extract the zip file
3. Copy `mysql-connector-java-x.x.xx.jar` to your project `lib` directory

### Option 2: Place in Project Directory
1. Put the JAR file in: `lib\`
2. Create the `lib` folder if it doesn't exist

## Compile and Run

### With JDBC Driver in lib Directory:
```powershell
# Create output directory
mkdir -p target/classes

# Compile
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" -d target/classes src/main/java/tapnpay/*.java

# Run
java -cp "target/classes;lib/mysql-connector-j-9.4.0.jar" tapnpay.TapNpay
```

### With JDBC Driver in lib Folder:
```powershell
# Compile
javac -cp ".;lib\mysql-connector-j-9.4.0.jar" -d target/classes src/main/java/tapnpay/*.java

# Run
java -cp "target/classes;lib\mysql-connector-j-9.4.0.jar" tapnpay.TapNpay
```

## Database Configuration

The application connects to MySQL with these settings:
- **Host**: localhost
- **Port**: 3306
- **Database**: tapnpay5
- **Username**: root
- **Password**: (empty)

To change these settings, edit `src/main/java/tapnpay/SignUp.java`:
```java
String url = "jdbc:mysql://localhost:3306/tapnpay5";
String dbUser = "root";
String dbPassword = "";
```

## Testing

1. Run the application
2. Click "Sign Up" from the login screen
3. Fill in:
   - Full Name
   - Email
   - Password
4. Click "Sign Up" button
5. You should see "Account created successfully!"
6. Check MySQL to verify the user was added:
   ```sql
   USE tapnpay5;
   SELECT * FROM CUSTOMER_INFO;
   ```

## Troubleshooting

### Error: "MySQL JDBC Driver not found"
- Download and add mysql-connector-java.jar to classpath

### Error: "Communications link failure"
- Make sure MySQL server is running
- Check that the port is 3306

### Error: "Access denied for user 'root'"
- Verify your MySQL username and password
- Update credentials in SignUp.java

### Error: "Unknown database 'tapnpay'"
- Run the database_setup.sql script
- Or manually create the database in MySQL

