import java.awt.*;
import java.sql.*;
import java.util.Random;
import javax.swing.*;

public class Home extends javax.swing.JFrame {
    private String userEmail;
    private String userType;
    private int customerId;
    private String customerName;
    private JLabel lblName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JLabel lblCardId;
    private JButton btnGenerateCard;
    private JTabbedPane tabbedPane;
    private JLabel lblBalance;
    private JLabel lblCardUser;
    private JLabel lblCardIdDisplay;
    private JLabel lblCardPhone;
    private JLabel lblCardEmail;

    public Home(String email, String userType) {
        this.userEmail = email;
        this.userType = userType;
        initComponents();
        loadUserData();
    }

    private void initComponents() {
        setTitle("TapNpay - Home");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(850, 550);
        setResizable(false);
        setLayout(new BorderLayout());

        // Top Panel with navigation buttons
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(153, 153, 255));
        topPanel.setPreferredSize(new Dimension(850, 80));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JButton btnHome = new JButton("Home");
        btnHome.setPreferredSize(new Dimension(100, 80));
        btnHome.setBackground(new Color(153, 153, 255));
        btnHome.addActionListener(evt -> tabbedPane.setSelectedIndex(0));

        JButton btnAccount = new JButton("Account");
        btnAccount.setPreferredSize(new Dimension(100, 80));
        btnAccount.setBackground(new Color(153, 153, 255));
        btnAccount.addActionListener(evt -> tabbedPane.setSelectedIndex(1));

        JButton btnTransactions = new JButton("Transactions");
        btnTransactions.setPreferredSize(new Dimension(120, 80));
        btnTransactions.setBackground(new Color(153, 153, 255));
        btnTransactions.addActionListener(evt -> tabbedPane.setSelectedIndex(2));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(430, 80));
        
        JButton btnLogout = new JButton("Log out");
        btnLogout.setPreferredSize(new Dimension(110, 60));
        btnLogout.setBackground(new Color(153, 153, 255));
        btnLogout.addActionListener(evt -> {
            Login login = new Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            dispose();
        });
        
        lblBalance = new JLabel("Balance: ₹0.00");
        lblBalance.setForeground(Color.WHITE);
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        rightPanel.add(lblBalance);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(btnLogout);

        topPanel.add(btnHome);
        topPanel.add(btnAccount);
        topPanel.add(btnTransactions);
        topPanel.add(rightPanel);

        // Tabbed Pane for content
        tabbedPane = new JTabbedPane();

        // Home Tab
        JPanel homePanel = new JPanel(null);
        homePanel.setBackground(new Color(240, 248, 255));

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(new Color(153, 255, 153));
        cardPanel.setBounds(40, 50, 350, 250);
        cardPanel.setLayout(new GridLayout(6, 1, 5, 5));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblCardTitle = new JLabel("TapNpay Card", JLabel.CENTER);
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCardTitle.setForeground(new Color(0, 100, 0));
        
        lblCardUser = new JLabel("User: Loading...");
        lblCardUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        lblCardIdDisplay = new JLabel("Card ID: Not Generated");
        lblCardIdDisplay.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblCardPhone = new JLabel("Phone: Not Set");
        lblCardPhone.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        lblCardEmail = new JLabel("Email: Loading...");
        lblCardEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel lblCardExpiry = new JLabel("Valid Till: 12/2029");
        lblCardExpiry.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        cardPanel.add(lblCardTitle);
        cardPanel.add(lblCardUser);
        cardPanel.add(lblCardIdDisplay);
        cardPanel.add(lblCardPhone);
        cardPanel.add(lblCardEmail);
        cardPanel.add(lblCardExpiry);

        JButton btnSendMoney = new JButton("Send to card money");
        btnSendMoney.setBounds(450, 50, 250, 60);
        btnSendMoney.setBackground(new Color(100, 149, 237));
        btnSendMoney.setForeground(Color.WHITE);
        btnSendMoney.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton btnWithdraw = new JButton("Withdraw from card");
        btnWithdraw.setBounds(450, 130, 250, 60);
        btnWithdraw.setBackground(new Color(100, 149, 237));
        btnWithdraw.setForeground(Color.WHITE);
        btnWithdraw.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton btnSeeTransactions = new JButton("See transactions");
        btnSeeTransactions.setBounds(450, 210, 250, 60);
        btnSeeTransactions.setBackground(new Color(100, 149, 237));
        btnSeeTransactions.setForeground(Color.WHITE);
        btnSeeTransactions.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeeTransactions.addActionListener(evt -> tabbedPane.setSelectedIndex(2));
        
        JButton btnTapPay = new JButton("TAP & PAY");
        btnTapPay.setBounds(450, 290, 250, 60);
        btnTapPay.setBackground(new Color(220, 20, 60));
        btnTapPay.setForeground(Color.WHITE);
        btnTapPay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnTapPay.addActionListener(evt -> discoverAndPay());
        
        JButton btnAddMoney = new JButton("Add Money (UPI)");
        btnAddMoney.setBounds(450, 370, 250, 60);
        btnAddMoney.setBackground(new Color(34, 139, 34));
        btnAddMoney.setForeground(Color.WHITE);
        btnAddMoney.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddMoney.addActionListener(evt -> addMoneyViaUPI());

        homePanel.add(cardPanel);
        homePanel.add(btnSendMoney);
        homePanel.add(btnWithdraw);
       // homePanel.add(btnSeeTransactions);
        homePanel.add(btnTapPay);
        homePanel.add(btnAddMoney);

        // Account Tab
        JPanel accountPanel = new JPanel();
        accountPanel.setBackground(new Color(240, 248, 255));
        accountPanel.setLayout(new BorderLayout());
        accountPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Account info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 2, 15, 15));
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

        // Card ID
        JLabel lblCardIdTitle = new JLabel("Card ID:");
        lblCardIdTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCardIdTitle.setForeground(new Color(51, 153, 255));
        
        lblCardId = new JLabel("Not Generated");
        lblCardId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCardId.setForeground(new Color(0, 0, 0));
        
        // Update Account button
        JButton btnUpdateAccount = new JButton("Update Account");
        btnUpdateAccount.setBackground(new Color(0, 102, 102));
        btnUpdateAccount.setForeground(Color.WHITE);
        btnUpdateAccount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUpdateAccount.addActionListener(evt -> {
            String phoneNumber = txtPhone.getText().trim();
            if (phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a phone number!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!phoneNumber.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit phone number!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updatePhoneNumber(phoneNumber);
            }
        });
        
        // Generate Card ID button
        btnGenerateCard = new JButton("Generate Card");
        btnGenerateCard.setBackground(new Color(0, 102, 102));
        btnGenerateCard.setForeground(Color.WHITE);
        btnGenerateCard.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerateCard.addActionListener(evt -> {
            generateCardId();
        });

        infoPanel.add(lblNameTitle);
        infoPanel.add(lblName);
        infoPanel.add(lblEmailTitle);
        infoPanel.add(txtEmail);
        infoPanel.add(lblPhoneTitle);
        infoPanel.add(txtPhone);
        infoPanel.add(lblCardIdTitle);
        infoPanel.add(lblCardId);
        infoPanel.add(new JLabel());
        infoPanel.add(btnUpdateAccount);
        infoPanel.add(new JLabel());
        infoPanel.add(btnGenerateCard);
        
        accountPanel.add(infoPanel, BorderLayout.CENTER);

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
        
        // Load transactions when tab is selected
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) {
                loadTransactions(txtTransactions);
            }
        });

        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Account", accountPanel);
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
                customerName = rs.getString("FULL_NAME");
                lblName.setText(customerName);
                txtEmail.setText(userEmail);
                
                // Update card display
                lblCardUser.setText("User: " + customerName);
                lblCardEmail.setText("Email: " + userEmail);
            }
            rs.close();
            pst.close();
            
            // Load balance
            String balanceQuery = "SELECT balance FROM Account WHERE customer_id = ?";
            PreparedStatement pstBalance = conn.prepareStatement(balanceQuery);
            pstBalance.setInt(1, customerId);
            ResultSet rsBalance = pstBalance.executeQuery();
            if (rsBalance.next()) {
                double balance = rsBalance.getDouble("balance");
                lblBalance.setText(String.format("Balance: ₹%.2f", balance));
            }
            rsBalance.close();
            pstBalance.close();
            
            // Get account info if exists
            String accountQuery = "SELECT phone_number, card_id FROM Account WHERE customer_id = ?";
            PreparedStatement pst2 = conn.prepareStatement(accountQuery);
            pst2.setInt(1, customerId);
            ResultSet rs2 = pst2.executeQuery();
            
            boolean hasPhone = false;
            boolean hasCardId = false;
            
            if (rs2.next()) {
                String phone = rs2.getString("phone_number");
                String cardId = rs2.getString("card_id");
                
                if (phone != null && !phone.isEmpty()) {
                    txtPhone.setText(phone);
                    hasPhone = true;
                    // Update card display with phone
                    lblCardPhone.setText("Phone: " + phone);
                }
                if (cardId != null && !cardId.isEmpty()) {
                    lblCardId.setText(cardId);
                    btnGenerateCard.setEnabled(false);
                    hasCardId = true;
                    // Update card display with card ID
                    lblCardIdDisplay.setText("Card: " + cardId);
                }
            }
            
            rs2.close();
            pst2.close();
            conn.close();
            
            // Check if account details are incomplete
            if (!hasPhone || !hasCardId) {
                SwingUtilities.invokeLater(() -> {
                    int result = JOptionPane.showConfirmDialog(this, 
                        "Please complete your account details!\nWould you like to fill them now?", 
                        "Account Details Required", 
                        JOptionPane.OK_CANCEL_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    if (result == JOptionPane.OK_OPTION) {
                        tabbedPane.setSelectedIndex(1); // Switch to Account tab
                    }
                });
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePhoneNumber(String phoneNumber) {
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
                String updateQuery = "UPDATE Account SET phone_number = ? WHERE customer_id = ?";
                PreparedStatement pst2 = conn.prepareStatement(updateQuery);
                pst2.setString(1, phoneNumber);
                pst2.setInt(2, customerId);
                pst2.executeUpdate();
                pst2.close();
            } else {
                // Create new account
                String insertQuery = "INSERT INTO Account (customer_id, phone_number, user_type) VALUES (?, ?, ?)";
                PreparedStatement pst2 = conn.prepareStatement(insertQuery);
                pst2.setInt(1, customerId);
                pst2.setString(2, phoneNumber);
                pst2.setString(3, userType);
                pst2.executeUpdate();
                pst2.close();
            }
            
            rs.close();
            pst.close();
            conn.close();
            
            lblCardPhone.setText("Phone: " + phoneNumber);
            JOptionPane.showMessageDialog(this, "Phone number updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating phone number: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateCardId() {
        String url = "jdbc:mysql://localhost:3306/tapnpay5";
        String dbUser = "root";
        String dbPassword = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            
            // Generate random 16-digit card ID
            Random random = new Random();
            StringBuilder cardId = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                cardId.append(random.nextInt(10));
                if ((i + 1) % 4 == 0 && i < 15) {
                    cardId.append("-");
                }
            }
            String generatedCardId = cardId.toString();
            
            // Check if account exists
            String checkQuery = "SELECT id FROM Account WHERE customer_id = ?";
            PreparedStatement pst = conn.prepareStatement(checkQuery);
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                // Update existing account
                String updateQuery = "UPDATE Account SET card_id = ? WHERE customer_id = ?";
                PreparedStatement pst2 = conn.prepareStatement(updateQuery);
                pst2.setString(1, generatedCardId);
                pst2.setInt(2, customerId);
                pst2.executeUpdate();
                pst2.close();
            } else {
                // Create new account
                String insertQuery = "INSERT INTO Account (customer_id, card_id, user_type) VALUES (?, ?, ?)";
                PreparedStatement pst2 = conn.prepareStatement(insertQuery);
                pst2.setInt(1, customerId);
                pst2.setString(2, generatedCardId);
                pst2.setString(3, userType);
                pst2.executeUpdate();
                pst2.close();
            }
            
            rs.close();
            pst.close();
            conn.close();
            
            lblCardId.setText(generatedCardId);
            lblCardIdDisplay.setText("Card: " + generatedCardId);
            btnGenerateCard.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Card ID generated successfully!\nCard ID: " + generatedCardId, "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating card ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addMoneyViaUPI() {
        // Create dialog for UPI payment
        JDialog dialog = new JDialog(this, "Add Money via UPI", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblAmount = new JLabel("Amount:");
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField txtAmount = new JTextField();
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblUPI = new JLabel("UPI ID:");
        lblUPI.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField txtUPI = new JTextField();
        txtUPI.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUPI.setText("yourname@upi");
        
        inputPanel.add(lblAmount);
        inputPanel.add(txtAmount);
        inputPanel.add(lblUPI);
        inputPanel.add(txtUPI);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnPay = new JButton("Pay via UPI");
        btnPay.setBackground(new Color(34, 139, 34));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        btnPay.addActionListener(e -> {
            String amountStr = txtAmount.getText().trim();
            String upiId = txtUPI.getText().trim();
            
            if (amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (upiId.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter UPI ID!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (amount > 10000) {
                    JOptionPane.showMessageDialog(dialog, "Maximum amount is ₹10,000!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                dialog.dispose();
                processUPIPayment(amount, upiId);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnPay);
        buttonPanel.add(btnCancel);
        
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void processUPIPayment(double amount, String upiId) {
        JDialog progressDialog = new JDialog(this, "Processing UPI Payment...", true);
        progressDialog.setSize(300, 120);
        progressDialog.setLocationRelativeTo(this);
        progressDialog.setLayout(new BorderLayout());
        
        JLabel lblProcessing = new JLabel("<html><center>Verifying UPI ID...<br>Please wait...</center></html>", JLabel.CENTER);
        lblProcessing.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        progressDialog.add(lblProcessing, BorderLayout.CENTER);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressDialog.add(progressBar, BorderLayout.SOUTH);
        
        new Thread(() -> {
            try {
                // Simulate UPI verification delay
                Thread.sleep(2000);
                
                // Add money to account
                String url = "jdbc:mysql://localhost:3306/tapnpay5";
                String dbUser = "root";
                String dbPassword = "";
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
                
                String updateQuery = "UPDATE Account SET balance = balance + ? WHERE customer_id = ?";
                PreparedStatement pst = conn.prepareStatement(updateQuery);
                pst.setDouble(1, amount);
                pst.setInt(2, customerId);
                int rowsUpdated = pst.executeUpdate();
                
                pst.close();
                conn.close();
                
                SwingUtilities.invokeLater(() -> {
                    progressDialog.dispose();
                    
                    if (rowsUpdated > 0) {
                        refreshBalance();
                        JOptionPane.showMessageDialog(this,
                            String.format("Money added successfully!\n₹%.2f added to your account via UPI", amount),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Failed to add money. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    progressDialog.dispose();
                    JOptionPane.showMessageDialog(this,
                        "UPI payment failed: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
        
        progressDialog.setVisible(true);
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
                int receiverId = rs.getInt("receiver_id");
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
                txtTransactions.setCaretPosition(0); // Scroll to top
            }
            
            rs.close();
            pst.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            txtTransactions.setText("Error loading transactions: " + e.getMessage());
        }
    }
    
    private void discoverAndPay() {
        JDialog dialog = new JDialog(this, "Discovering Payment Requests...", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JLabel lblScanning = new JLabel("Scanning for payment requests...", JLabel.CENTER);
        lblScanning.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dialog.add(lblScanning, BorderLayout.CENTER);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        dialog.add(progressBar, BorderLayout.SOUTH);
        
        new Thread(() -> {
            PaymentClient.discoverPaymentRequests(new PaymentClient.DiscoveryCallback() {
                @Override
                public void onRequestFound(PaymentClient.PaymentRequest request) {
                    // Request found
                }
                
                @Override
                public void onDiscoveryComplete(java.util.List<PaymentClient.PaymentRequest> requests) {
                    SwingUtilities.invokeLater(() -> {
                        dialog.dispose();
                        
                        if (requests.isEmpty()) {
                            JOptionPane.showMessageDialog(Home.this, 
                                "No payment requests found.\nMake sure a driver is accepting payments.", 
                                "No Requests Found", 
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            showPaymentOptions(requests);
                        }
                    });
                }
            });
        }).start();
        
        dialog.setVisible(true);
    }
    
    private void showPaymentOptions(java.util.List<PaymentClient.PaymentRequest> requests) {
        Object[] options = requests.toArray();
        PaymentClient.PaymentRequest selected = (PaymentClient.PaymentRequest) JOptionPane.showInputDialog(
            this,
            "Select a payment request:",
            "Available Payments",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Confirm payment of ₹%.2f to %s (%s)?",  
                    selected.amount, selected.driverName, selected.vehicleNumber),
                "Confirm Payment",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                processPayment(selected);
            }
        }
    }
    
    private void processPayment(PaymentClient.PaymentRequest request) {
        JDialog progressDialog = new JDialog(this, "Processing Payment...", true);
        progressDialog.setSize(300, 100);
        progressDialog.setLocationRelativeTo(this);
        progressDialog.setLayout(new BorderLayout());
        
        JLabel lblProcessing = new JLabel("Processing payment...", JLabel.CENTER);
        progressDialog.add(lblProcessing, BorderLayout.CENTER);
        
        new Thread(() -> {
            PaymentClient.PaymentResult result = PaymentClient.makePayment(request, customerId, customerName);
            
            SwingUtilities.invokeLater(() -> {
                progressDialog.dispose();
                
                if (result.success) {
                    refreshBalance();
                    JOptionPane.showMessageDialog(this,
                        String.format("Payment successful!\nPaid ₹%.2f to %s", 
                            request.amount, request.driverName),
                        "Payment Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String errorMsg = result.message;
                    if (errorMsg.contains("balance") || errorMsg.contains("Insufficient")) {
                        errorMsg = "Insufficient balance!\nPlease add money to your account.";
                    }
                    JOptionPane.showMessageDialog(this,
                        errorMsg,
                        "Payment Failed",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
        
        progressDialog.setVisible(true);
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

        java.awt.EventQueue.invokeLater(() -> new Home("test@example.com", "customer").setVisible(true));
    }
}

