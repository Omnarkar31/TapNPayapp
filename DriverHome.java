import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class DriverHome extends javax.swing.JFrame {
    private String userEmail;
    private int customerId;
    private String driverName;
    private String vehicleNumber;
    private JLabel lblName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtVehicleNumber;
    private PaymentServer paymentServer;
    private JLabel lblBalance;

    public DriverHome(String email) {
        this.userEmail = email;
        initComponents();
        loadUserData();
    }

    private void initComponents() {
        setTitle("TapNpay - Driver Home");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(850, 550);
        setResizable(false);
        setLayout(new BorderLayout());

        // Top Panel with navigation
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(153, 153, 255));
        topPanel.setPreferredSize(new Dimension(850, 80));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JTabbedPane tabbedPane = new JTabbedPane();

        JButton btnAccount = new JButton("Account");
        btnAccount.setPreferredSize(new Dimension(100, 80));
        btnAccount.setBackground(new Color(153, 153, 255));
        btnAccount.addActionListener(evt -> tabbedPane.setSelectedIndex(0));

        JButton btnTransactions = new JButton("Transactions");
        btnTransactions.setPreferredSize(new Dimension(120, 80));
        btnTransactions.setBackground(new Color(153, 153, 255));
        btnTransactions.addActionListener(evt -> tabbedPane.setSelectedIndex(1));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(530, 80));
        
        lblBalance = new JLabel("Balance: ₹0.00");
        lblBalance.setForeground(Color.WHITE);
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JButton btnLogout = new JButton("Log out");
        btnLogout.setPreferredSize(new Dimension(110, 60));
        btnLogout.setBackground(new Color(153, 153, 255));
        btnLogout.addActionListener(evt -> {
            Login login = new Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            dispose();
        });
        
        rightPanel.add(lblBalance);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(btnLogout);

        topPanel.add(btnAccount);
        topPanel.add(btnTransactions);
        topPanel.add(rightPanel);

        // Account Tab Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Account info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 2, 15, 15));
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Name
        JLabel lblNameTitle = new JLabel("Full Name:");
        lblNameTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNameTitle.setForeground(new Color(51, 153, 255));
        
        lblName = new JLabel("____________________");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblName.setForeground(new Color(0, 0, 0));

        // Email
        JLabel lblEmailTitle = new JLabel("Email:");
        lblEmailTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEmailTitle.setForeground(new Color(51, 153, 255));
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtEmail.setEditable(false);
        txtEmail.setBackground(new Color(240, 240, 240));

        // Phone
        JLabel lblPhoneTitle = new JLabel("Phone Number:");
        lblPhoneTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPhoneTitle.setForeground(new Color(51, 153, 255));
        
        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPhone.setToolTipText("Enter your phone number");

        // Vehicle Number
        JLabel lblVehicleTitle = new JLabel("Vehicle Number:");
        lblVehicleTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblVehicleTitle.setForeground(new Color(51, 153, 255));
        
        txtVehicleNumber = new JTextField();
        txtVehicleNumber.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtVehicleNumber.setToolTipText("Enter your vehicle/auto number");

        // Update Account button
        JButton btnUpdateAccount = new JButton("Update Account");
        btnUpdateAccount.setBackground(new Color(0, 102, 102));
        btnUpdateAccount.setForeground(Color.WHITE);
        btnUpdateAccount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUpdateAccount.addActionListener(evt -> {
            String phoneNumber = txtPhone.getText().trim();
            String vehicleNumber = txtVehicleNumber.getText().trim();
            
            if (phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a phone number!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!phoneNumber.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit phone number!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (vehicleNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your vehicle number!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updateAccount(phoneNumber, vehicleNumber);
            }
        });

        infoPanel.add(lblNameTitle);
        infoPanel.add(lblName);
        infoPanel.add(lblEmailTitle);
        infoPanel.add(txtEmail);
        infoPanel.add(lblPhoneTitle);
        infoPanel.add(txtPhone);
        infoPanel.add(lblVehicleTitle);
        infoPanel.add(txtVehicleNumber);
        infoPanel.add(new JLabel());
        infoPanel.add(btnUpdateAccount);
        
        // Receive Payment Panel
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        paymentPanel.setBackground(new Color(240, 248, 255));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Receive Payment"));
        
        JLabel lblAmount = new JLabel("Amount:");
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField txtAmount = new JTextField(10);
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnStartReceiving = new JButton("Start Receiving");
        btnStartReceiving.setBackground(new Color(34, 139, 34));
        btnStartReceiving.setForeground(Color.WHITE);
        btnStartReceiving.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JButton btnStopReceiving = new JButton("Stop");
        btnStopReceiving.setBackground(new Color(220, 53, 69));
        btnStopReceiving.setForeground(Color.WHITE);
        btnStopReceiving.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnStopReceiving.setEnabled(false);
        
        JLabel lblStatus = new JLabel("Not receiving");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(Color.GRAY);
        
        btnStartReceiving.addActionListener(evt -> {
            String amountStr = txtAmount.getText().trim();
            if (amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                startPaymentServer(amount, lblStatus, btnStartReceiving, btnStopReceiving, txtAmount);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnStopReceiving.addActionListener(evt -> {
            stopPaymentServer(lblStatus, btnStartReceiving, btnStopReceiving, txtAmount);
        });
        
        paymentPanel.add(lblAmount);
        paymentPanel.add(txtAmount);
        paymentPanel.add(btnStartReceiving);
        paymentPanel.add(btnStopReceiving);
        paymentPanel.add(lblStatus);
        
        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(paymentPanel, BorderLayout.SOUTH);

        // Transactions Tab
        JPanel transactionsPanel = new JPanel();
        transactionsPanel.setBackground(new Color(240, 248, 255));
        transactionsPanel.setLayout(new BorderLayout());
        
        JLabel lblTransactions = new JLabel("Transaction History", JLabel.CENTER);
        lblTransactions.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTransactions.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        transactionsPanel.add(lblTransactions, BorderLayout.NORTH);
        
        JTextArea txtTransactions = new JTextArea("Loading transactions...");
        txtTransactions.setEditable(false);
        txtTransactions.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTransactions.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(txtTransactions);
        transactionsPanel.add(scrollPane, BorderLayout.CENTER);
        
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                loadTransactions(txtTransactions);
            }
        });

        tabbedPane.addTab("Account", contentPanel);
        tabbedPane.addTab("Transactions", transactionsPanel);

        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
    }

    private void loadUserData() {
        String url = "jdbc:mysql://localhost:3306/tapnpay5";
        String dbUser = "root";
        String dbPassword = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            
            // Get customer info
            String query = "SELECT id, FULL_NAME FROM CUSTOMER_INFO WHERE Email = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, userEmail);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                customerId = rs.getInt("id");
                driverName = rs.getString("FULL_NAME");
                lblName.setText(driverName);
                txtEmail.setText(userEmail);
            }
            rs.close();
            pst.close();
            
            // Get account info and balance
            String accountQuery = "SELECT phone_number, vehicle_number, balance FROM Account WHERE customer_id = ?";
            PreparedStatement pst2 = conn.prepareStatement(accountQuery);
            pst2.setInt(1, customerId);
            ResultSet rs2 = pst2.executeQuery();
            
            boolean hasPhone = false;
            boolean hasVehicle = false;
            
            if (rs2.next()) {
                String phone = rs2.getString("phone_number");
                String vehicle = rs2.getString("vehicle_number");
                double balance = rs2.getDouble("balance");
                
                lblBalance.setText(String.format("Balance: ₹%.2f", balance));
                
                if (phone != null && !phone.isEmpty()) {
                    txtPhone.setText(phone);
                    hasPhone = true;
                }
                if (vehicle != null && !vehicle.isEmpty()) {
                    txtVehicleNumber.setText(vehicle);
                    vehicleNumber = vehicle;
                    hasVehicle = true;
                }
            }
            
            rs2.close();
            pst2.close();
            conn.close();
            
            // Check if account details are incomplete
            if (!hasPhone || !hasVehicle) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, 
                        "Please complete your driver account details!\nFill in your phone number and vehicle number.", 
                        "Account Details Required", 
                        JOptionPane.INFORMATION_MESSAGE);
                });
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateAccount(String phoneNumber, String vehicleNumber) {
        String url = "jdbc:mysql://localhost:3306/tapnpay5";
        String dbUser = "root";
        String dbPassword = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            
            // Check if account exists
            String checkQuery = "SELECT id FROM Account WHERE customer_id = ?";
            PreparedStatement pst = conn.prepareStatement(checkQuery);
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                // Update existing account
                String updateQuery = "UPDATE Account SET phone_number = ?, vehicle_number = ? WHERE customer_id = ?";
                PreparedStatement pst2 = conn.prepareStatement(updateQuery);
                pst2.setString(1, phoneNumber);
                pst2.setString(2, vehicleNumber);
                pst2.setInt(3, customerId);
                pst2.executeUpdate();
                pst2.close();
            } else {
                // Create new account
                String insertQuery = "INSERT INTO Account (customer_id, phone_number, vehicle_number, user_type) VALUES (?, ?, ?, 'driver')";
                PreparedStatement pst2 = conn.prepareStatement(insertQuery);
                pst2.setInt(1, customerId);
                pst2.setString(2, phoneNumber);
                pst2.setString(3, vehicleNumber);
                pst2.executeUpdate();
                pst2.close();
            }
            
            rs.close();
            pst.close();
            conn.close();
            
            JOptionPane.showMessageDialog(this, "Account updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating account: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshBalance() {
        String url = "jdbc:mysql://localhost:3306/tapnpay5";
        String dbUser = "root";
        String dbPassword = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            
            String balanceQuery = "SELECT balance FROM Account WHERE customer_id = ?";
            PreparedStatement pst = conn.prepareStatement(balanceQuery);
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                lblBalance.setText(String.format("Balance: ₹%.2f", balance));
            }
            
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadTransactions(JTextArea txtTransactions) {
        String url = "jdbc:mysql://localhost:3306/tapnpay5";
        String dbUser = "root";
        String dbPassword = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            
            String query = "SELECT t.*, " +
                          "sender.FULL_NAME as sender_name, " +
                          "receiver.FULL_NAME as receiver_name " +
                          "FROM TRANSACTIONS t " +
                          "JOIN CUSTOMER_INFO sender ON t.sender_id = sender.id " +
                          "JOIN CUSTOMER_INFO receiver ON t.receiver_id = receiver.id " +
                          "WHERE t.sender_id = ? OR t.receiver_id = ? " +
                          "ORDER BY t.transaction_date DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, customerId);
            pst.setInt(2, customerId);
            ResultSet rs = pst.executeQuery();
            
            StringBuilder transactions = new StringBuilder();
            transactions.append("=== TRANSACTION HISTORY ===\n\n");
            
            boolean hasTransactions = false;
            while (rs.next()) {
                hasTransactions = true;
                int senderId = rs.getInt("sender_id");
                double amount = rs.getDouble("amount");
                String senderName = rs.getString("sender_name");
                String receiverName = rs.getString("receiver_name");
                String vehicleNumber = rs.getString("receiver_vehicle_number");
                String date = rs.getString("transaction_date");
                
                String type = (senderId == customerId) ? "SENT" : "RECEIVED";
                String otherParty = (senderId == customerId) ? receiverName : senderName;
                
                transactions.append(String.format("[%s] %s\n", type, date));
                transactions.append(String.format("Amount: ₹%.2f\n", amount));
                transactions.append(String.format("%s: %s\n", 
                    (senderId == customerId) ? "To" : "From", otherParty));
                
                if (vehicleNumber != null && !vehicleNumber.isEmpty()) {
                    transactions.append(String.format("Vehicle: %s\n", vehicleNumber));
                }
                
                transactions.append("\n" + "-".repeat(50) + "\n\n");
            }
            
            if (!hasTransactions) {
                txtTransactions.setText("No transactions yet.");
            } else {
                txtTransactions.setText(transactions.toString());
                txtTransactions.setCaretPosition(0);
            }
            
            rs.close();
            pst.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            txtTransactions.setText("Error loading transactions: " + e.getMessage());
        }
    }
    
    private void startPaymentServer(double amount, JLabel lblStatus, JButton btnStart, JButton btnStop, JTextField txtAmount) {
        if (vehicleNumber == null || vehicleNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please update your vehicle number first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        paymentServer = new PaymentServer(customerId, driverName, vehicleNumber, amount, new PaymentServer.PaymentCallback() {
            @Override
            public void onPaymentReceived(String customerName, double paidAmount) {
                refreshBalance();
                JOptionPane.showMessageDialog(DriverHome.this, 
                    String.format("Payment received!\nFrom: %s\nAmount: ₹%.2f", customerName, paidAmount), 
                    "Payment Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                lblStatus.setText("Payment received!");
                lblStatus.setForeground(new Color(0, 128, 0));
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                txtAmount.setEnabled(true);
                txtAmount.setText("");
            }
            
            @Override
            public void onError(String error) {
                JOptionPane.showMessageDialog(DriverHome.this, error, "Error", JOptionPane.ERROR_MESSAGE);
                lblStatus.setText("Error occurred");
                lblStatus.setForeground(Color.RED);
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                txtAmount.setEnabled(true);
            }
            
            @Override
            public void onServerStarted() {
                lblStatus.setText(String.format("Waiting for payment of ₹%.2f...", amount));
                lblStatus.setForeground(new Color(0, 100, 200));
            }
        });
        
        paymentServer.start();
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        txtAmount.setEnabled(false);
    }
    
    private void stopPaymentServer(JLabel lblStatus, JButton btnStart, JButton btnStop, JTextField txtAmount) {
        if (paymentServer != null) {
            paymentServer.stopServer();
            paymentServer = null;
        }
        lblStatus.setText("Not receiving");
        lblStatus.setForeground(Color.GRAY);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        txtAmount.setEnabled(true);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> new DriverHome("driver@example.com").setVisible(true));
    }
}

