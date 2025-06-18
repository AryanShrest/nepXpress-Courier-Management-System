package nepxpress.model;

import java.sql.Timestamp;

/**
 * A class representing branch information
 */
public class Branch {
    private int id;
    private String branchCode;
    private String branchName;
    private String location;
    private String contactNumber;
    private String email;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    /**
     * Default constructor
     */
    public Branch() {
        this.status = "Active";
    }
    
    /**
     * Constructor for Branch
     * 
     * @param id Branch ID
     * @param branchCode Branch code
     * @param branchName Branch name
     * @param location Branch location
     * @param contactNumber Contact number
     * @param email Email address
     */
    public Branch(int id, String branchCode, String branchName, String location, 
                  String contactNumber, String email) {
        this.id = id;
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.location = location;
        this.contactNumber = contactNumber;
        this.email = email;
        this.status = "Active";
    }
    
    /**
     * Constructor for Branch without ID
     * 
     * @param branchCode Branch code
     * @param branchName Branch name
     * @param location Branch location
     * @param contactNumber Contact number
     * @param email Email address
     */
    public Branch(String branchCode, String branchName, String location, 
                  String contactNumber, String email) {
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.location = location;
        this.contactNumber = contactNumber;
        this.email = email;
        this.status = "Active";
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getBranchCode() {
        return branchCode;
    }
    
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return branchName;
    }
} 