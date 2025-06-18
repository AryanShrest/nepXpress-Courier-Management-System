package nepxpress.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    /**
     * Creates a user with minimal information (used for rider registrations)
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     * @param mobile Mobile number
     * @param accountType Account type (usually "rider")
     * @return The user ID if successful, -1 otherwise
     */
    public int createUser(String firstName, String lastName, String email, String mobile, String accountType) {
        String sql = "INSERT INTO users (first_name, surname, email_or_mobile, account_type) " +
                     "VALUES (?, ?, ?, ?)";
        
        String contactInfo = email;
        if (email == null || email.isEmpty()) {
            contactInfo = mobile;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, contactInfo);
            pstmt.setString(4, accountType);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Return the inserted ID
                    }
                }
            }
            
            return -1;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public boolean createUser(String firstName, String surname, String emailOrMobile, 
                            String password, String dateOfBirth, String gender, String accountType) {
        String sql = "INSERT INTO users (first_name, surname, email_or_mobile, password, date_of_birth, gender, account_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, surname);
            pstmt.setString(3, emailOrMobile);
            pstmt.setString(4, password); // Note: In production, this should be hashed
            pstmt.setDate(5, Date.valueOf(dateOfBirth));
            pstmt.setString(6, gender);
            pstmt.setString(7, accountType);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean validateLogin(String emailOrMobile, String password) {
        String sql = "SELECT id FROM users WHERE email_or_mobile = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, emailOrMobile);
            pstmt.setString(2, password); // Note: In production, this should be hashed
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if user exists with given credentials
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateUser(int userId, String firstName, String surname, String emailOrMobile) {
        String sql = "UPDATE users " +
                    "SET first_name = ?, surname = ?, email_or_mobile = ? " +
                    "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, surname);
            pstmt.setString(3, emailOrMobile);
            pstmt.setInt(4, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword); // Note: In production, this should be hashed
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deletes a user account
     * @param userId The ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT first_name, surname, email_or_mobile FROM users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String user = String.format("%s %s (%s)",
                    rs.getString("first_name"),
                    rs.getString("surname"),
                    rs.getString("email_or_mobile")
                );
                users.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    public boolean emailOrMobileExists(String emailOrMobile) {
        String sql = "SELECT id FROM users WHERE email_or_mobile = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, emailOrMobile);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if email/mobile already exists
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getUserIdByMobile(String mobileNumber) {
        String sql = "SELECT id FROM users WHERE email_or_mobile = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, mobileNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1; // Return -1 if user not found
    }
    
    /**
     * Gets the user ID by email or mobile number
     * @param emailOrMobile The email or mobile number to search for
     * @return The user ID if found, -1 otherwise
     */
    public int getUserIdByEmailOrMobile(String emailOrMobile) {
        String sql = "SELECT id FROM users WHERE email_or_mobile = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, emailOrMobile);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            
            return -1;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public String getUserAccountType(int userId) {
        String sql = "SELECT account_type FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("account_type");
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
} 