package nepxpress.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nepxpress.database.DatabaseConnection;
import nepxpress.model.Branch;

/**
 * Data Access Object for Branch operations
 */
public class BranchDAO {
    
    /**
     * Insert a new branch into the database
     * 
     * @param branch The branch object to insert
     * @return The generated ID of the new branch, or -1 if insertion failed
     */
    public int insertBranch(Branch branch) {
        String sql = "INSERT INTO branches (branch_code, branch_name, location, contact_number, email) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, branch.getBranchCode());
            pstmt.setString(2, branch.getBranchName());
            pstmt.setString(3, branch.getLocation());
            pstmt.setString(4, branch.getContactNumber());
            pstmt.setString(5, branch.getEmail());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error inserting branch: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Update an existing branch in the database
     * 
     * @param branch The branch object with updated information
     * @return true if the update was successful, false otherwise
     */
    public boolean updateBranch(Branch branch) {
        String sql = "UPDATE branches SET branch_name = ?, location = ?, contact_number = ?, " +
                     "email = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, branch.getBranchName());
            pstmt.setString(2, branch.getLocation());
            pstmt.setString(3, branch.getContactNumber());
            pstmt.setString(4, branch.getEmail());
            pstmt.setString(5, branch.getStatus());
            pstmt.setInt(6, branch.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating branch: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete a branch from the database
     * 
     * @param branchId The ID of the branch to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteBranch(int branchId) {
        String sql = "DELETE FROM branches WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, branchId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting branch: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get a branch by its ID
     * 
     * @param branchId The ID of the branch to retrieve
     * @return The branch object if found, null otherwise
     */
    public Branch getBranchById(int branchId) {
        String sql = "SELECT * FROM branches WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, branchId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBranchFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting branch by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get a branch by its code
     * 
     * @param branchCode The code of the branch to retrieve
     * @return The branch object if found, null otherwise
     */
    public Branch getBranchByCode(String branchCode) {
        String sql = "SELECT * FROM branches WHERE branch_code = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, branchCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBranchFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting branch by code: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all branches from the database
     * 
     * @return List of all branches
     */
    public List<Branch> getAllBranches() {
        List<Branch> branches = new ArrayList<>();
        String sql = "SELECT * FROM branches ORDER BY branch_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                branches.add(extractBranchFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all branches: " + e.getMessage());
            e.printStackTrace();
        }
        
        return branches;
    }
    
    /**
     * Get all active branches from the database
     * 
     * @return List of all active branches
     */
    public List<Branch> getActiveBranches() {
        List<Branch> branches = new ArrayList<>();
        String sql = "SELECT * FROM branches WHERE status = 'Active' ORDER BY branch_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                branches.add(extractBranchFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active branches: " + e.getMessage());
            e.printStackTrace();
        }
        
        return branches;
    }
    
    /**
     * Helper method to extract a Branch object from a ResultSet
     * 
     * @param rs The ResultSet containing branch data
     * @return A Branch object
     * @throws SQLException If there's an error accessing the ResultSet
     */
    private Branch extractBranchFromResultSet(ResultSet rs) throws SQLException {
        Branch branch = new Branch();
        branch.setId(rs.getInt("id"));
        branch.setBranchCode(rs.getString("branch_code"));
        branch.setBranchName(rs.getString("branch_name"));
        branch.setLocation(rs.getString("location"));
        branch.setContactNumber(rs.getString("contact_number"));
        branch.setEmail(rs.getString("email"));
        branch.setStatus(rs.getString("status"));
        branch.setCreatedAt(rs.getTimestamp("created_at"));
        branch.setUpdatedAt(rs.getTimestamp("updated_at"));
        return branch;
    }
    
    /**
     * Check if a branch code already exists
     * 
     * @param branchCode The branch code to check
     * @return true if the branch code exists, false otherwise
     */
    public boolean isBranchCodeExists(String branchCode) {
        String sql = "SELECT COUNT(*) FROM branches WHERE branch_code = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, branchCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking branch code: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if a branch email already exists
     * 
     * @param email The email to check
     * @param excludeId Optional branch ID to exclude from the check (for updates)
     * @return true if the email exists, false otherwise
     */
    public boolean isEmailExists(String email, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM branches WHERE email = ?";
        
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
            System.err.println("Error checking branch email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
} 