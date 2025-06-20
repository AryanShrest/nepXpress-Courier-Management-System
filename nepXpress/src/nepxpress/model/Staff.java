package nepxpress.model;

import java.sql.Timestamp;

/**
 * A class representing staff information
 */
public class Staff {
    private int id;
    private String staffId;
    private Integer userId;
    private int branchId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields not in the database
    private String branchName;
    
    /**
     * Default constructor
     */
    public Staff() {
        this.status = "Active";
    }
    
    /**
     * Constructor for Staff
     * 
     * @param id Staff ID
     * @param staffId Staff identifier
     * @param branchId Branch ID
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     * @param phone Phone number
     * @param role Staff role
     */
    public Staff(int id, String staffId, int branchId, String firstName, 
                 String lastName, String email, String phone, String role) {
        this.id = id;
        this.staffId = staffId;
        this.branchId = branchId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = "Active";
    }
    
    /**
     * Constructor for Staff without ID
     * 
     * @param staffId Staff identifier
     * @param branchId Branch ID
     * @param firstName First name
     * @param lastName Last name
     * @param email Email address
     * @param phone Phone number
     * @param role Staff role
     */
    public Staff(String staffId, int branchId, String firstName, 
                 String lastName, String email, String phone, String role) {
        this.staffId = staffId;
        this.branchId = branchId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = "Active";
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getStaffId() {
        return staffId;
    }
    
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public int getBranchId() {
        return branchId;
    }
    
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
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
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
} 