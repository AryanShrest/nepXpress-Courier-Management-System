package nepxpress.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserSessionDAO {
    
    public String createSession(int userId, String ipAddress, String userAgent) {
        String sessionToken = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24); // Session expires in 24 hours
        
        String sql = """
            INSERT INTO user_sessions (user_id, session_token, ip_address, user_agent, expires_at)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, sessionToken);
            pstmt.setString(3, ipAddress);
            pstmt.setString(4, userAgent);
            pstmt.setTimestamp(5, Timestamp.valueOf(expiresAt));
            
            pstmt.executeUpdate();
            return sessionToken;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean validateSession(String sessionToken) {
        String sql = """
            SELECT user_id, expires_at, is_active 
            FROM user_sessions 
            WHERE session_token = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sessionToken);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp expiresAt = rs.getTimestamp("expires_at");
                    boolean isActive = rs.getBoolean("is_active");
                    
                    // Check if session is still valid
                    return isActive && expiresAt.after(new Timestamp(System.currentTimeMillis()));
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void updateLastActivity(String sessionToken) {
        String sql = """
            UPDATE user_sessions 
            SET last_activity = CURRENT_TIMESTAMP 
            WHERE session_token = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sessionToken);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void invalidateSession(String sessionToken) {
        String sql = """
            UPDATE user_sessions 
            SET is_active = FALSE 
            WHERE session_token = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sessionToken);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void invalidateAllUserSessions(int userId) {
        String sql = """
            UPDATE user_sessions 
            SET is_active = FALSE 
            WHERE user_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Integer getUserIdFromSession(String sessionToken) {
        String sql = """
            SELECT user_id 
            FROM user_sessions 
            WHERE session_token = ? AND is_active = TRUE 
            AND expires_at > CURRENT_TIMESTAMP
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, sessionToken);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void cleanupExpiredSessions() {
        String sql = "DELETE FROM user_sessions WHERE expires_at < CURRENT_TIMESTAMP";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 