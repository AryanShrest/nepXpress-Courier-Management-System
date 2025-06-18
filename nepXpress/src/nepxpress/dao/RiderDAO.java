package nepxpress.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import nepxpress.database.DatabaseConnection;
import nepxpress.model.RiderInfo;

/**
 * Data Access Object for Rider operations
 */
public class RiderDAO {
    
    /**
     * Insert a new rider into the database
     * 
     * @param rider The rider object to insert
     * @return The generated ID of the new rider, or -1 if insertion failed
     */
    public int insertRider(RiderInfo rider) {
        String sql = "INSERT INTO riders (branch_id, full_name, contact_info, vehicle_type, license_number, vehicle_registration, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, rider.getBranchId());
            pstmt.setString(2, rider.getFullName());
            pstmt.setString(3, rider.getContactInfo());
            pstmt.setString(4, rider.getVehicleType());
            pstmt.setString(5, rider.getLicenseNumber());
            pstmt.setString(6, rider.getVehicleRegistration());
            pstmt.setString(7, rider.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error inserting rider: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Update an existing rider in the database
     * 
     * @param rider The rider object with updated information
     * @return true if the update was successful, false otherwise
     */
    public boolean updateRider(RiderInfo rider) {
        String sql = "UPDATE riders SET branch_id = ?, full_name = ?, contact_info = ?, " +
                     "vehicle_type = ?, license_number = ?, vehicle_registration = ?, " +
                     "status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rider.getBranchId());
            pstmt.setString(2, rider.getFullName());
            pstmt.setString(3, rider.getContactInfo());
            pstmt.setString(4, rider.getVehicleType());
            pstmt.setString(5, rider.getLicenseNumber());
            pstmt.setString(6, rider.getVehicleRegistration());
            pstmt.setString(7, rider.getStatus());
            pstmt.setInt(8, rider.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating rider: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete a rider from the database
     * 
     * @param riderId The ID of the rider to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteRider(int riderId) {
        String sql = "DELETE FROM riders WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, riderId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting rider: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get a rider by their ID
     * 
     * @param riderId The ID of the rider to retrieve
     * @return The rider object if found, null otherwise
     */
    public RiderInfo getRiderById(int riderId) {
        String sql = "SELECT r.*, b.branch_name FROM riders r " +
                     "LEFT JOIN branches b ON r.branch_id = b.id " +
                     "WHERE r.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, riderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractRiderFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting rider by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get a rider by their rider ID
     * 
     * @param riderId The rider ID of the rider to retrieve
     * @return The rider object if found, null otherwise
     */
    public RiderInfo getRiderByRiderId(String riderId) {
        String sql = "SELECT r.*, b.branch_name FROM riders r " +
                     "LEFT JOIN branches b ON r.branch_id = b.id " +
                     "WHERE r.rider_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, riderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractRiderFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting rider by rider ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all riders from the database
     * 
     * @return List of all riders
     */
    public List<RiderInfo> getAllRiders() {
        List<RiderInfo> riders = new ArrayList<>();
        String sql = "SELECT r.*, b.branch_name FROM riders r " +
                     "LEFT JOIN branches b ON r.branch_id = b.id " +
                     "ORDER BY r.full_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                riders.add(extractRiderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all riders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return riders;
    }
    
    /**
     * Get all riders for a specific branch
     * 
     * @param branchId The ID of the branch
     * @return List of riders for the branch
     */
    public List<RiderInfo> getRidersByBranch(int branchId) {
        List<RiderInfo> riders = new ArrayList<>();
        String sql = "SELECT r.*, b.branch_name FROM riders r " +
                     "LEFT JOIN branches b ON r.branch_id = b.id " +
                     "WHERE r.branch_id = ? " +
                     "ORDER BY r.full_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, branchId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    riders.add(extractRiderFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting riders by branch: " + e.getMessage());
            e.printStackTrace();
        }
        
        return riders;
    }
    
    /**
     * Get all active riders
     * 
     * @return List of active riders
     */
    public List<RiderInfo> getActiveRiders() {
        List<RiderInfo> riders = new ArrayList<>();
        String sql = "SELECT r.*, b.branch_name FROM riders r " +
                     "LEFT JOIN branches b ON r.branch_id = b.id " +
                     "WHERE r.status = 'Active' " +
                     "ORDER BY r.full_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                riders.add(extractRiderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active riders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return riders;
    }
    
    /**
     * Get all pending riders
     * 
     * @return List of pending riders
     */
    public List<RiderInfo> getPendingRiders() {
        List<RiderInfo> pendingRiders = new ArrayList<>();
        String sql = "SELECT * FROM riders WHERE status = 'Pending'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                RiderInfo rider = new RiderInfo();
                rider.setId(rs.getInt("id"));
                rider.setFullName(rs.getString("full_name"));
                rider.setContactInfo(rs.getString("contact_info"));
                rider.setVehicleType(rs.getString("vehicle_type"));
                rider.setVehicleRegistration(rs.getString("vehicle_registration"));
                // Set other fields as needed
                pendingRiders.add(rider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingRiders;
    }
    
    /**
     * Helper method to extract a RiderInfo object from a ResultSet
     * 
     * @param rs The ResultSet containing rider data
     * @return A RiderInfo object
     * @throws SQLException If there's an error accessing the ResultSet
     */
    private RiderInfo extractRiderFromResultSet(ResultSet rs) throws SQLException {
        RiderInfo rider = new RiderInfo();
        rider.setId(rs.getInt("id"));
        rider.setRiderId(rs.getString("rider_id"));
        rider.setBranchId(rs.getInt("branch_id"));
        rider.setFullName(rs.getString("full_name"));
        rider.setContactInfo(rs.getString("contact_info"));
        rider.setVehicleType(rs.getString("vehicle_type"));
        rider.setLicenseNumber(rs.getString("license_number"));
        rider.setVehicleRegistration(rs.getString("vehicle_registration"));
        rider.setStatus(rs.getString("status"));
        rider.setCreatedAt(rs.getTimestamp("created_at"));
        rider.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set branch name if available in the result set
        try {
            rider.setBranchName(rs.getString("branch_name"));
        } catch (SQLException e) {
            // Branch name not included in the result set, ignore
        }
        
        return rider;
    }
    
    /**
     * Check if a rider ID already exists
     * 
     * @param riderId The rider ID to check
     * @return true if the rider ID exists, false otherwise
     */
    public boolean isRiderIdExists(String riderId) {
        String sql = "SELECT COUNT(*) FROM riders WHERE rider_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, riderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking rider ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Generate a unique rider ID
     * 
     * @return A unique rider ID
     */
    public String generateRiderId() {
        String prefix = "RDR";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id;
        
        do {
            id = new StringBuilder(prefix);
            java.util.Random rnd = new java.util.Random();
            for (int i = 0; i < 7; i++) {
                id.append(chars.charAt(rnd.nextInt(chars.length())));
            }
        } while (isRiderIdExists(id.toString()));
        
        return id.toString();
    }
} 