package nepxpress.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RiderDAO {
    
    /**
     * Creates a new rider entry
     * @param userId The ID of the user who is becoming a rider
     * @param vehicleType The type of vehicle (e.g., "Motorcycle", "Scooter")
     * @param licenseNumber The rider's license number
     * @param vehicleRegistration The vehicle registration number
     * @return true if rider creation was successful, false otherwise
     */
    public boolean createRider(int userId, String vehicleType, String licenseNumber, String vehicleRegistration) {
        System.out.println("\n=== Creating new rider account ===");
        System.out.println("User ID: " + userId);
        System.out.println("Vehicle Type: " + vehicleType);
        System.out.println("License Number: " + licenseNumber);
        System.out.println("Vehicle Registration: " + vehicleRegistration);
        
        String sql = "INSERT INTO riders (user_id, vehicle_type, license_number, vehicle_registration, status) " +
                    "VALUES (?, ?, ?, ?, 'Inactive')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            System.out.println("Executing SQL query: " + sql);
            System.out.println("Parameters: [" + userId + ", " + vehicleType + ", " + licenseNumber + ", " + vehicleRegistration + ", 'Inactive']");
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, vehicleType);
            pstmt.setString(3, licenseNumber);
            pstmt.setString(4, vehicleRegistration);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            
            if (rowsAffected > 0) {
                System.out.println("Rider account created successfully");
                return true;
            } else {
                System.out.println("Failed to create rider account - no rows affected");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Database error while creating rider: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("=== End rider creation ===\n");
        }
    }
    
    /**
     * Updates a rider's status
     * @param riderId The ID of the rider
     * @param status The new status ("Active", "Inactive", or "Suspended")
     * @return true if update was successful, false otherwise
     */
    public boolean updateRiderStatus(int riderId, String status) {
        String sql = "UPDATE riders SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, riderId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets a rider by their user ID
     * @param userId The ID of the user
     * @return RiderInfo object containing rider details, or null if not found
     */
    public RiderInfo getRiderByUserId(int userId) {
        String sql = "SELECT r.*, u.first_name, u.surname, u.email_or_mobile " +
                    "FROM riders r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new RiderInfo(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("email_or_mobile"),
                        rs.getString("vehicle_type"),
                        rs.getString("license_number"),
                        rs.getString("vehicle_registration"),
                        rs.getString("status")
                    );
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets all active riders
     * @return List of active riders
     */
    public List<RiderInfo> getActiveRiders() {
        String sql = "SELECT r.*, u.first_name, u.surname, u.email_or_mobile " +
                    "FROM riders r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.status = 'Active'";
        
        List<RiderInfo> riders = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                riders.add(new RiderInfo(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("surname"),
                    rs.getString("email_or_mobile"),
                    rs.getString("vehicle_type"),
                    rs.getString("license_number"),
                    rs.getString("vehicle_registration"),
                    rs.getString("status")
                ));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return riders;
    }
    
    /**
     * Checks if a license number is already registered
     * @param licenseNumber The license number to check
     * @return true if the license number exists, false otherwise
     */
    public boolean isLicenseNumberTaken(String licenseNumber) {
        String sql = "SELECT COUNT(*) FROM riders WHERE license_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, licenseNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Checks if a vehicle registration number is already registered
     * @param vehicleRegistration The vehicle registration to check
     * @return true if the registration exists, false otherwise
     */
    public boolean isVehicleRegistrationTaken(String vehicleRegistration) {
        String sql = "SELECT COUNT(*) FROM riders WHERE vehicle_registration = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, vehicleRegistration);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 