import java.io.*;
import java.net.*;
import java.sql.*;
import javax.swing.SwingUtilities;

public class PaymentServer extends Thread {
    private int port = 9876;
    private double amount;
    private int driverId;
    private String driverName;
    private String vehicleNumber;
    private ServerSocket serverSocket;
    private boolean running = true;
    private PaymentCallback callback;

    public interface PaymentCallback {
        void onPaymentReceived(String customerName, double amount);
        void onError(String error);
        void onServerStarted();
    }

    public PaymentServer(int driverId, String driverName, String vehicleNumber, double amount, PaymentCallback callback) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.vehicleNumber = vehicleNumber;
        this.amount = amount;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            SwingUtilities.invokeLater(() -> callback.onServerStarted());
            
            // Start UDP broadcast
            new Thread(() -> broadcastPaymentRequest()).start();
            
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    handlePayment(clientSocket);
                } catch (SocketException e) {
                    // Socket closed
                    break;
                }
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> callback.onError("Server error: " + e.getMessage()));
        }
    }

    private void broadcastPaymentRequest() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            
            String message = "PAYMENT_REQUEST|" + driverId + "|" + driverName + "|" + vehicleNumber + "|" + amount;
            byte[] buffer = message.getBytes();
            
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            
            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, 9877);
                socket.send(packet);
                Thread.sleep(2000); // Broadcast every 2 seconds
            }
            
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePayment(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            String request = in.readLine();
            String[] parts = request.split("\\|");
            
            if (parts[0].equals("PAY")) {
                int customerId = Integer.parseInt(parts[1]);
                String customerName = parts[2];
                double paidAmount = Double.parseDouble(parts[3]);
                
                if (Math.abs(paidAmount - amount) < 0.01) {
                    // Process payment in database
                    String errorMessage = processPayment(customerId, customerName);
                    
                    if (errorMessage == null) {
                        out.println("SUCCESS");
                        SwingUtilities.invokeLater(() -> callback.onPaymentReceived(customerName, paidAmount));
                        stopServer();
                    } else {
                        out.println("FAILED|" + errorMessage);
                    }
                } else {
                    out.println("FAILED|Amount mismatch");
                }
            }
            
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String processPayment(int customerId, String customerName) {
        String url = "jdbc:mysql://localhost:3306/tapnpay5";
        String dbUser = "root";
        String dbPassword = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            conn.setAutoCommit(false);
            
            try {
                // Check customer balance first
                String checkQuery = "SELECT balance FROM Account WHERE customer_id = ?";
                PreparedStatement pstCheck = conn.prepareStatement(checkQuery);
                pstCheck.setInt(1, customerId);
                ResultSet rsCheck = pstCheck.executeQuery();
                
                if (!rsCheck.next()) {
                    rsCheck.close();
                    pstCheck.close();
                    conn.close();
                    return "Account not found";
                }
                
                double currentBalance = rsCheck.getDouble("balance");
                rsCheck.close();
                pstCheck.close();
                
                // Check if customer has sufficient balance
                if (currentBalance < amount) {
                    conn.close();
                    return "Insufficient balance";
                }
                
                // Deduct from customer balance
                String deductQuery = "UPDATE Account SET balance = balance - ? WHERE customer_id = ? AND balance >= ?";
                PreparedStatement pst1 = conn.prepareStatement(deductQuery);
                pst1.setDouble(1, amount);
                pst1.setInt(2, customerId);
                pst1.setDouble(3, amount);
                int rowsUpdated = pst1.executeUpdate();
                pst1.close();
                
                if (rowsUpdated == 0) {
                    conn.rollback();
                    conn.close();
                    return "Insufficient balance";
                }
                
                // Add to driver balance
                String addQuery = "UPDATE Account SET balance = balance + ? WHERE customer_id = ?";
                PreparedStatement pst2 = conn.prepareStatement(addQuery);
                pst2.setDouble(1, amount);
                pst2.setInt(2, driverId);
                pst2.executeUpdate();
                pst2.close();
                
                // Record transaction
                String transQuery = "INSERT INTO TRANSACTIONS (sender_id, receiver_id, amount, payment_type, receiver_name, receiver_vehicle_number) VALUES (?, ?, ?, 'TAP_PAY', ?, ?)";
                PreparedStatement pst3 = conn.prepareStatement(transQuery);
                pst3.setInt(1, customerId);
                pst3.setInt(2, driverId);
                pst3.setDouble(3, amount);
                pst3.setString(4, driverName);
                pst3.setString(5, vehicleNumber);
                pst3.executeUpdate();
                pst3.close();
                
                conn.commit();
                conn.close();
                return null; // Success
            } catch (SQLException e) {
                conn.rollback();
                conn.close();
                e.printStackTrace();
                return "Database error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "System error";
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

