package nepxpress.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import nepxpress.database.DatabaseConnection;

public class QueryRidersTest {
    public static void main(String[] args) {
        queryAllRiders();
        queryPendingRiders();
    }
    
    private static void queryAllRiders() {
        System.out.println("\n--- All Registered Riders ---");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String sql = "SELECT r.id, u.first_name, u.surname, u.email_or_mobile, " +
                         "r.vehicle_type, r.license_number, r.vehicle_registration, r.status, r.created_at " +
                         "FROM riders r " +
                         "JOIN users u ON r.user_id = u.id " +
                         "ORDER BY r.created_at DESC";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            System.out.println("ID | Name | Contact | Vehicle | License | Registration | Status | Created");
            System.out.println("-----------------------------------------------------------------------");
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("first_name") + " " + rs.getString("surname") + " | " +
                    rs.getString("email_or_mobile") + " | " +
                    rs.getString("vehicle_type") + " | " +
                    rs.getString("license_number") + " | " +
                    rs.getString("vehicle_registration") + " | " +
                    rs.getString("status") + " | " +
                    rs.getTimestamp("created_at")
                );
            }
            
            if (count == 0) {
                System.out.println("No riders found in the database.");
            } else {
                System.out.println("Total riders found: " + count);
            }
            
        } catch (SQLException e) {
            System.err.println("Error querying riders: " + e.getMessage());
            e.printStackTrace();
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
    
    private static void queryPendingRiders() {
        System.out.println("\n--- Pending Riders ---");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String sql = "SELECT r.id, u.first_name, u.surname, u.email_or_mobile, " +
                         "r.vehicle_type, r.license_number, r.vehicle_registration, r.created_at " +
                         "FROM riders r " +
                         "JOIN users u ON r.user_id = u.id " +
                         "WHERE r.status = 'Inactive' " +
                         "ORDER BY r.created_at DESC";
            
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            System.out.println("ID | Name | Contact | Vehicle | License | Registration | Created");
            System.out.println("-------------------------------------------------------------");
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("first_name") + " " + rs.getString("surname") + " | " +
                    rs.getString("email_or_mobile") + " | " +
                    rs.getString("vehicle_type") + " | " +
                    rs.getString("license_number") + " | " +
                    rs.getString("vehicle_registration") + " | " +
                    rs.getTimestamp("created_at")
                );
            }
            
            if (count == 0) {
                System.out.println("No pending riders found in the database.");
            } else {
                System.out.println("Total pending riders: " + count);
            }
            
        } catch (SQLException e) {
            System.err.println("Error querying pending riders: " + e.getMessage());
            e.printStackTrace();
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