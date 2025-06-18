package nepxpress.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import nepxpress.database.DatabaseConnection;

/**
 * Utility to fix rider password issues
 */
public class FixRiderPasswordTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Rider Password Fix Utility ===");
        System.out.print("Enter your mobile number: ");
        String mobileNumber = scanner.nextLine().trim();
        
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine().trim();
        
        if (updateRiderPassword(mobileNumber, newPassword)) {
            System.out.println("Password updated successfully! You can now login with your mobile number and new password.");
        } else {
            System.out.println("Failed to update password. Please check your mobile number or contact support.");
        }
        
        scanner.close();
    }
    
    /**
     * Updates a rider's password based on their mobile number
     * 
     * @param mobileNumber The rider's mobile number
     * @param newPassword The new password to set
     * @return true if successful, false otherwise
     */
    private static boolean updateRiderPassword(String mobileNumber, String newPassword) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            // First check if the user exists
            String checkSql = "SELECT id FROM users WHERE email_or_mobile = ? AND account_type = 'Rider'";
            stmt = conn.prepareStatement(checkSql);
            stmt.setString(1, mobileNumber);
            rs = stmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("No rider found with this mobile number.");
                return false;
            }
            
            int userId = rs.getInt("id");
            
            // Update the password
            String updateSql = "UPDATE users SET password = ? WHERE id = ?";
            stmt = conn.prepareStatement(updateSql);
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
} 