package nepxpress.model;

import java.sql.Timestamp;
import nepxpress.database.RiderDAO;

/**
 * A class representing rider information
 */
public class RiderInfo {
    private int id;
    private String riderId;
    private Integer userId;
    private int branchId;
    private String fullName;
    private String contactInfo;
    private String vehicleType;
    private String licenseNumber;
    private String vehicleRegistration;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields not in the database
    private String branchName;
    
    /**
     * Default constructor
     */
    public RiderInfo() {
        this.status = "Active";
    }
    
    /**
     * Constructor for RiderInfo
     * @param id Rider ID
     * @param riderId Rider identifier
     * @param branchId Branch ID
     * @param fullName Full name of the rider
     * @param contactInfo Email or mobile number
     * @param vehicleType Type of vehicle
     * @param licenseNumber License number
     * @param vehicleRegistration Vehicle registration number
     */
    public RiderInfo(int id, String riderId, int branchId, String fullName, String contactInfo, 
                    String vehicleType, String licenseNumber, String vehicleRegistration) {
        this.id = id;
        this.riderId = riderId;
        this.branchId = branchId;
        this.fullName = fullName;
        this.contactInfo = contactInfo;
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
        this.vehicleRegistration = vehicleRegistration;
        this.status = "Active";
    }
    
    /**
     * Constructor for RiderInfo without ID
     * @param riderId Rider identifier
     * @param branchId Branch ID
     * @param fullName Full name of the rider
     * @param contactInfo Email or mobile number
     * @param vehicleType Type of vehicle
     * @param licenseNumber License number
     * @param vehicleRegistration Vehicle registration number
     */
    public RiderInfo(String riderId, int branchId, String fullName, String contactInfo, 
                    String vehicleType, String licenseNumber, String vehicleRegistration) {
        this.riderId = riderId;
        this.branchId = branchId;
        this.fullName = fullName;
        this.contactInfo = contactInfo;
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
        this.vehicleRegistration = vehicleRegistration;
        this.status = "Active";
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getRiderId() {
        return riderId;
    }
    
    public void setRiderId(String riderId) {
        this.riderId = riderId;
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
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
    public String getVehicleRegistration() {
        return vehicleRegistration;
    }
    
    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
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
        return "RiderInfo{" + 
               "id=" + id + 
               ", riderId='" + riderId + '\'' +
               ", branchId=" + branchId +
               ", fullName='" + fullName + '\'' + 
               ", contactInfo='" + contactInfo + '\'' + 
               ", vehicleType='" + vehicleType + '\'' + 
               ", licenseNumber='" + licenseNumber + '\'' + 
               ", vehicleRegistration='" + vehicleRegistration + '\'' + 
               ", status='" + status + '\'' +
               '}';
    }
} 