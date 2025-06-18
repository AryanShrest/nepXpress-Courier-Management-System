package nepxpress.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nepxpress.database.DatabaseConnection;
import nepxpress.database.RiderDAO;
import nepxpress.model.RiderInfo;

public class ShowAllPendingRidersTest {
    public static void main(String[] args) {
        System.out.println("\n======= ALL PENDING RIDER REQUESTS =======\n");
        
        try {
            // Get database connection
            Connection conn = DatabaseConnection.getConnection();
            
            // SQL query to get all pending riders (status = Inactive)
            String sql = "SELECT r.id, r.rider_name, u.mobile, r.vehicle_type, r.license_number, " +
                         "r.vehicle_registration, r.created_at " +
                         "FROM riders r " +
                         "JOIN users u ON r.user_id = u.id " +
                         "WHERE r.status = 'Inactive' " +
                         "ORDER BY r.created_at DESC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Print header
            System.out.printf("%-5s | %-20s | %-25s | %-15s | %-15s | %-15s | %-20s\n", 
                    "ID", "Name", "Contact", "Vehicle", "License", "Registration", "Created");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            
            int count = 0;
            // Print each rider
            while (rs.next()) {
                count++;
                System.out.printf("%-5d | %-20s | %-25s | %-15s | %-15s | %-15s | %-20s\n",
                        rs.getInt("id"),
                        rs.getString("rider_name"),
                        rs.getString("mobile"),
                        rs.getString("vehicle_type"),
                        rs.getString("license_number"),
                        rs.getString("vehicle_registration"),
                        rs.getTimestamp("created_at"));
            }
            
            System.out.println("--------------------------------------------------------------------------------------------------------");
            System.out.println("Total pending riders: " + count);
            
            if (count == 0) {
                System.out.println("No pending riders found.");
            }
            
            // Get RiderDAO instance to show admin actions
            RiderDAO riderDAO = new RiderDAO();
            List<RiderInfo> pendingRiders = riderDAO.getPendingRiders();
            
            if (!pendingRiders.isEmpty()) {
                System.out.println("\n===== ADMIN ACTIONS =====");
                System.out.println("To approve a rider, admin should click the 'Approve' button in the Rider Approvals panel.");
                System.out.println("To reject a rider, admin should click the 'Reject' button in the Rider Approvals panel.");
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error retrieving pending riders: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 