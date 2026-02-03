// package tapnpay;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.SwingUtilities;

public class PaymentClient {
    
    public static class PaymentRequest {
        public int driverId;
        public String driverName;
        public String vehicleNumber;
        public double amount;
        public String ipAddress;
        
        public PaymentRequest(int driverId, String driverName, String vehicleNumber, double amount, String ipAddress) {
            this.driverId = driverId;
            this.driverName = driverName;
            this.vehicleNumber = vehicleNumber;
            this.amount = amount;
            this.ipAddress = ipAddress;
        }
        
        @Override
        public String toString() {
            return String.format("Pay ₹%.2f to %s (%s)", amount, driverName, vehicleNumber);
        }
    }
    
    public interface DiscoveryCallback {
        void onRequestFound(PaymentRequest request);
        void onDiscoveryComplete(List<PaymentRequest> requests);
    }
    
    public static void discoverPaymentRequests(DiscoveryCallback callback) {
        new Thread(() -> {
            List<PaymentRequest> requests = new ArrayList<>();
            
            try {
                DatagramSocket socket = new DatagramSocket(9877);
                socket.setSoTimeout(5000); // 5 seconds timeout
                
                byte[] buffer = new byte[1024];
                long startTime = System.currentTimeMillis();
                
                while (System.currentTimeMillis() - startTime < 5000) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        
                        String message = new String(packet.getData(), 0, packet.getLength());
                        String[] parts = message.split("\\|");
                        
                        if (parts[0].equals("PAYMENT_REQUEST")) {
                            int driverId = Integer.parseInt(parts[1]);
                            String driverName = parts[2];
                            String vehicleNumber = parts[3];
                            double amount = Double.parseDouble(parts[4]);
                            String ipAddress = packet.getAddress().getHostAddress();
                            
                            PaymentRequest request = new PaymentRequest(driverId, driverName, vehicleNumber, amount, ipAddress);
                            
                            // Avoid duplicates
                            boolean isDuplicate = false;
                            for (PaymentRequest existing : requests) {
                                if (existing.driverId == driverId) {
                                    isDuplicate = true;
                                    break;
                                }
                            }
                            
                            if (!isDuplicate) {
                                requests.add(request);
                                SwingUtilities.invokeLater(() -> callback.onRequestFound(request));
                            }
                        }
                    } catch (SocketTimeoutException e) {
                        // Continue listening
                    }
                }
                
                socket.close();
                SwingUtilities.invokeLater(() -> callback.onDiscoveryComplete(requests));
                
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> callback.onDiscoveryComplete(requests));
            }
        }).start();
    }
    
    public static class PaymentResult {
        public boolean success;
        public String message;
        
        public PaymentResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
    
    public static PaymentResult makePayment(PaymentRequest request, int customerId, String customerName) {
        try {
            Socket socket = new Socket(request.ipAddress, 9876);
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String paymentMessage = "PAY|" + customerId + "|" + customerName + "|" + request.amount;
            out.println(paymentMessage);
            
            String response = in.readLine();
            
            socket.close();
            
            if (response != null && response.equals("SUCCESS")) {
                return new PaymentResult(true, "Payment successful");
            } else if (response != null && response.startsWith("FAILED")) {
                String[] parts = response.split("\\|");
                String reason = parts.length > 1 ? parts[1] : "Payment failed";
                return new PaymentResult(false, reason);
            } else {
                return new PaymentResult(false, "Payment failed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentResult(false, "Connection error: " + e.getMessage());
        }
    }
}

