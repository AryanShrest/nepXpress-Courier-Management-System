package nepxpress.dao;

import com.courier.database.DatabaseConnection;
import com.courier.model.PickupRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PickupRequestDAO {
    
    public boolean insertPickupRequest(PickupRequest request) {
        String sql = "INSERT INTO pickup_requests (name, address, mobile_no, pickup_date, password) VALUES (?, ?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, request.getName());
            pstmt.setString(2, request.getAddress());
            pstmt.setString(3, request.getMobileNo());
            pstmt.setDate(4, request.getPickupDate());
            pstmt.setString(5, hashPassword(request.getPassword()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error inserting pickup request: " + e.getMessage());
            return false;
        }
    }
    
    public boolean insertCancelRequest(String name, String address, String mobileNo, String reason, String password) {
        String sql = "INSERT INTO cancel_requests (name, address, mobile_no, reason, password) VALUES (?, ?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, mobileNo);
            pstmt.setString(4, reason);
            pstmt.setString(5, hashPassword(password));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error inserting cancel request: " + e.getMessage());
            return false;
        }
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return password; // Return plain password if hashing fails
        }
    }
}