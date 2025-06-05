package nepxpress.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetDAO {
    
    /**
     * Creates a password reset token for a user
     * @param userId The ID of the user requesting the password reset
     * @param expiryHours Number of hours before the token expires
     * @return The generated reset token, or null if creation failed
     */
    public String createResetToken(int userId, int expiryHours) {
        String sql = """
            INSERT INTO password_resets (user_id, token, expires_at)
            VALUES (?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Generate a unique token
            String token = UUID.randomUUID().toString();
            
            // Set expiry time
            LocalDateTime expiryTime = LocalDateTime.now().plusHours(expiryHours);
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, token);
            pstmt.setTimestamp(3, Timestamp.valueOf(expiryTime));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0 ? token : null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Validates a password reset token
     * @param token The token to validate
     * @return The user ID if token is valid, or -1 if invalid
     */
    public int validateResetToken(String token) {
        String sql = """
            SELECT user_id FROM password_resets 
            WHERE token = ? 
            AND used = FALSE 
            AND expires_at > NOW()
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, token);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Marks a reset token as used
     * @param token The token to mark as used
     * @return true if successful, false otherwise
     */
    public boolean markTokenAsUsed(String token) {
        String sql = "UPDATE password_resets SET used = TRUE WHERE token = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, token);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets the reset history for a user
     * @param userId The ID of the user
     * @return A string containing the reset history, or null if error occurs
     */
    public String getResetHistory(int userId) {
        String sql = """
            SELECT created_at, used, expires_at 
            FROM password_resets 
            WHERE user_id = ? 
            ORDER BY created_at DESC
        """;
        
        StringBuilder history = new StringBuilder();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    history.append("Reset requested on: ")
                          .append(rs.getTimestamp("created_at"))
                          .append(", Status: ")
                          .append(rs.getBoolean("used") ? "Used" : "Not Used")
                          .append(", Expired on: ")
                          .append(rs.getTimestamp("expires_at"))
                          .append("\n");
                }
            }
            
            return history.toString();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
} 