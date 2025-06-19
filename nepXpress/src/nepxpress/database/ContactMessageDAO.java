package nepxpress.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactMessageDAO {
    public boolean insertContactMessage(String fullName, String mobileNumber, String email, String message) {
        String sql = "INSERT INTO contact_messages (full_name, mobile_number, email, message) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, mobileNumber);
            pstmt.setString(3, email);
            pstmt.setString(4, message);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 