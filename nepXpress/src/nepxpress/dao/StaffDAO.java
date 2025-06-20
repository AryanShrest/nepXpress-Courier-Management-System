package nepxpress.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nepxpress.database.DatabaseConnection;
import nepxpress.model.Staff;

/**
 * Data Access Object for Staff operations
 */
public class StaffDAO {
    
    /**
     * Insert a new staff member into the database
     * 
     * @param staff The staff object to insert
     * @return The generated ID of the new staff member, or -1 if insertion failed
     */
    public int insertStaff(Staff staff) {
        String sql = "INSERT INTO staff (staff_id, branch_id, first_name, last_name, email, phone, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, staff.getStaffId());
            pstmt.setInt(2, staff.getBranchId());
            pstmt.setString(3, staff.getFirstName());
            pstmt.setString(4, staff.getLastName());
            pstmt.setString(5, staff.getEmail());
            pstmt.setString(6, staff.getPhone());
            pstmt.setString(7, staff.getRole());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error inserting staff: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Update an existing staff member in the database
     * 
     * @param staff The staff object with updated information
     * @return true if the update was successful, false otherwise
     */
    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET branch_id = ?, first_name = ?, last_name = ?, " +
                     "email = ?, phone = ?, role = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, staff.getBranchId());
            pstmt.setString(2, staff.getFirstName());
            pstmt.setString(3, staff.getLastName());
            pstmt.setString(4, staff.getEmail());
            pstmt.setString(5, staff.getPhone());
            pstmt.setString(6, staff.getRole());
            pstmt.setString(7, staff.getStatus());
            pstmt.setInt(8, staff.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete a staff member from the database
     * 
     * @param staffId The ID of the staff member to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteStaff(int staffId) {
        String sql = "DELETE FROM staff WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, staffId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get a staff member by their ID
     * 
     * @param staffId The ID of the staff member to retrieve
     * @return The staff object if found, null otherwise
     */
    public Staff getStaffById(int staffId) {
        String sql = "SELECT s.*, b.branch_name FROM staff s " +
                     "LEFT JOIN branches b ON s.branch_id = b.id " +
                     "WHERE s.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, staffId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractStaffFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting staff by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get a staff member by their staff ID
     * 
     * @param staffId The staff ID of the staff member to retrieve
     * @return The staff object if found, null otherwise
     */
    public Staff getStaffByStaffId(String staffId) {
        String sql = "SELECT s.*, b.branch_name FROM staff s " +
                     "LEFT JOIN branches b ON s.branch_id = b.id " +
                     "WHERE s.staff_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, staffId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractStaffFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting staff by staff ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all staff members from the database
     * 
     * @return List of all staff members
     */
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT s.*, b.branch_name FROM staff s " +
                     "LEFT JOIN branches b ON s.branch_id = b.id " +
                     "ORDER BY s.first_name, s.last_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                staffList.add(extractStaffFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all staff: " + e.getMessage());
            e.printStackTrace();
        }
        
        return staffList;
    }
    
    /**
     * Get all staff members for a specific branch
     * 
     * @param branchId The ID of the branch
     * @return List of staff members for the branch
     */
    public List<Staff> getStaffByBranch(int branchId) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT s.*, b.branch_name FROM staff s " +
                     "LEFT JOIN branches b ON s.branch_id = b.id " +
                     "WHERE s.branch_id = ? " +
                     "ORDER BY s.first_name, s.last_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, branchId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    staffList.add(extractStaffFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting staff by branch: " + e.getMessage());
            e.printStackTrace();
        }
        
        return staffList;
    }
    
    /**
     * Helper method to extract a Staff object from a ResultSet
     * 
     * @param rs The ResultSet containing staff data
     * @return A Staff object
     * @throws SQLException If there's an error accessing the ResultSet
     */
    private Staff extractStaffFromResultSet(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getInt("id"));
        staff.setStaffId(rs.getString("staff_id"));
        staff.setUserId(rs.getInt("user_id"));
        staff.setBranchId(rs.getInt("branch_id"));
        staff.setFirstName(rs.getString("first_name"));
        staff.setLastName(rs.getString("last_name"));
        staff.setEmail(rs.getString("email"));
        staff.setPhone(rs.getString("phone"));
        staff.setRole(rs.getString("role"));
        staff.setStatus(rs.getString("status"));
        staff.setCreatedAt(rs.getTimestamp("created_at"));
        staff.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set branch name if available in the result set
        try {
            staff.setBranchName(rs.getString("branch_name"));
        } catch (SQLException e) {
            // Branch name not included in the result set, ignore
        }
        
        return staff;
    }
    
    /**
     * Check if a staff ID already exists
     * 
     * @param staffId The staff ID to check
     * @return true if the staff ID exists, false otherwise
     */
    public boolean isStaffIdExists(String staffId) {
        String sql = "SELECT COUNT(*) FROM staff WHERE staff_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, staffId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking staff ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if a staff email already exists
     * 
     * @param email The email to check
     * @param excludeId Optional staff ID to exclude from the check (for updates)
     * @return true if the email exists, false otherwise
     */
    public boolean isEmailExists(String email, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM staff WHERE email = ?";
        
        if (excludeId != null) {
            sql += " AND id != ?";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            if (excludeId != null) {
                pstmt.setInt(2, excludeId);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking staff email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Generate a unique staff ID
     * 
     * @return A unique staff ID
     */
    public String generateStaffId() {
        String prefix = "STF";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id;
        
        do {
            id = new StringBuilder(prefix);
            java.util.Random rnd = new java.util.Random();
            for (int i = 0; i < 7; i++) {
                id.append(chars.charAt(rnd.nextInt(chars.length())));
            }
        } while (isStaffIdExists(id.toString()));
        
        return id.toString();
    }
} 