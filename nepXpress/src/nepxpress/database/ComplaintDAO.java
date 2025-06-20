package nepxpress.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ComplaintDAO {
    public boolean insertComplaint(String parcelNumber, String nature, String description, String fullName, String mobileNumber, String email) {
        String sql = "INSERT INTO complaints (parcel_number, nature, description, full_name, mobile_number, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, parcelNumber);
            pstmt.setString(2, nature);
            pstmt.setString(3, description);
            pstmt.setString(4, fullName);
            pstmt.setString(5, mobileNumber);
            pstmt.setString(6, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 