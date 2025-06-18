package nepxpress.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nepxpress.database.DatabaseConnection;

public class RiderDAO {
    
    /**
     * Creates a new rider with pending status
     * 
     * @param fullName The rider's full name
     * @param emailOrMobile The rider's email or mobile number
     * @param vehicleType The rider's vehicle type
     * @param licenseNumber The rider's license number
     * @param vehicleRegistration The rider's vehicle registration number
     * @param password The rider's password
     * @return true if the rider was created successfully, false otherwise
     */
    public boolean createRider(String fullName, String emailOrMobile, String vehicleType, 
                              String licenseNumber, String vehicleRegistration, String password) {
        return createRiderWithStatus(fullName, emailOrMobile, vehicleType, licenseNumber, 
                                    vehicleRegistration, password, "Inactive");
    }
    
    /**
     * Creates a new rider with the specified status
     * 
     * @param fullName The rider's full name
     * @param emailOrMobile The rider's email or mobile number
     * @param vehicleType The rider's vehicle type
     * @param licenseNumber The rider's license number
     * @param vehicleRegistration The rider's vehicle registration number
     * @param password The rider's password
     * @param status The rider's status (e.g., "Active", "Inactive", "Suspended")
     * @return true if the rider was created successfully, false otherwise
     */
    public boolean createRiderWithStatus(String fullName, String emailOrMobile, String vehicleType, 
                                        String licenseNumber, String vehicleRegistration, 
                                        String password, String status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            // Split fullName into first_name and surname
            String[] nameParts = fullName.split(" ", 2);
            String firstName = nameParts[0];
            String surname = nameParts.length > 1 ? nameParts[1] : "";
            
            // First create the user
            String userSql = "INSERT INTO users (first_name, surname, email_or_mobile, password, date_of_birth, gender, account_type) VALUES (?, ?, ?, ?, CURDATE(), 'Male', 'Rider')";
            stmt = conn.prepareStatement(userSql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, firstName);
            stmt.setString(2, surname);
            stmt.setString(3, emailOrMobile);
            stmt.setString(4, password);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated user ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    
                    // Now create the rider
                    String riderSql = "INSERT INTO riders (user_id, vehicle_type, license_number, vehicle_registration, status) " +
                                     "VALUES (?, ?, ?, ?, ?)";
                    
                    stmt = conn.prepareStatement(riderSql);
                    stmt.setInt(1, userId);
                    stmt.setString(2, vehicleType);
                    stmt.setString(3, licenseNumber);
                    stmt.setString(4, vehicleRegistration);
                    stmt.setString(5, status);
                    
                    return stmt.executeUpdate() > 0;
                }
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error creating rider: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Updates a rider's status
     * 
     * @param riderId The rider's ID
     * @param status The new status (e.g., "Active", "Inactive", "Suspended")
     * @return true if the status was updated successfully, false otherwise
     */
    public boolean updateRiderStatus(int riderId, String status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String sql = "UPDATE riders SET status = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, riderId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating rider status: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gets a list of pending riders
     * 
     * @return A list of pending riders
     */
    public List<RiderInfo> getPendingRiders() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<RiderInfo> pendingRiders = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String sql = "SELECT r.id, CONCAT(u.first_name, ' ', u.surname) AS full_name, u.email_or_mobile, " +
                         "r.vehicle_type, r.license_number, r.vehicle_registration " +
                         "FROM riders r JOIN users u ON r.user_id = u.id " +
                         "WHERE r.status IN ('Inactive', 'Pending')";
            System.out.println("Fetching pending riders...");
            System.out.println("SQL: " + sql);
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                System.out.println("Fetched: " + rs.getInt("id") + ", " + rs.getString("full_name") + ", " + rs.getString("email_or_mobile"));
                RiderInfo rider = new RiderInfo(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("email_or_mobile"),
                    rs.getString("vehicle_type"),
                    rs.getString("license_number"),
                    rs.getString("vehicle_registration")
                );
                pendingRiders.add(rider);
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending riders: " + e.getMessage());
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
        
        return pendingRiders;
    }

    /**
     * Gets a RiderInfo object by userId.
     * @param userId The user ID to look up
     * @return RiderInfo if found, null otherwise
     */
    public RiderInfo getRiderByUserId(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.id, CONCAT(u.first_name, ' ', u.surname) AS full_name, u.email_or_mobile, " +
                         "r.vehicle_type, r.license_number, r.vehicle_registration " +
                         "FROM riders r JOIN users u ON r.user_id = u.id " +
                         "WHERE r.user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new RiderInfo(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("email_or_mobile"),
                    rs.getString("vehicle_type"),
                    rs.getString("license_number"),
                    rs.getString("vehicle_registration")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting rider by userId: " + e.getMessage());
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
        return null;
    }

    /**
     * Checks if a license number is already registered.
     * @param licenseNumber The license number to check
     * @return true if taken, false otherwise
     */
    public boolean isLicenseNumberTaken(String licenseNumber) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT id FROM riders WHERE license_number = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, licenseNumber);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking license number: " + e.getMessage());
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

    /**
     * Checks if a vehicle registration is already registered.
     * @param vehicleRegistration The registration to check
     * @return true if taken, false otherwise
     */
    public boolean isVehicleRegistrationTaken(String vehicleRegistration) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT id FROM riders WHERE vehicle_registration = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, vehicleRegistration);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking vehicle registration: " + e.getMessage());
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