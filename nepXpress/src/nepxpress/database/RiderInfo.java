package nepxpress.database;

/**
 * Data class to hold rider information
 */
public class RiderInfo {
    private int id;
    private String fullName;
    private String emailOrMobile;
    private String vehicleType;
    private String licenseNumber;
    private String vehicleRegistration;
    
    /**
     * Constructor
     * 
     * @param id The rider's ID
     * @param fullName The rider's full name
     * @param emailOrMobile The rider's email or mobile number
     * @param vehicleType The rider's vehicle type
     * @param licenseNumber The rider's license number
     * @param vehicleRegistration The rider's vehicle registration number
     */
    public RiderInfo(int id, String fullName, String emailOrMobile, String vehicleType, 
                    String licenseNumber, String vehicleRegistration) {
        this.id = id;
        this.fullName = fullName;
        this.emailOrMobile = emailOrMobile;
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
        this.vehicleRegistration = vehicleRegistration;
    }
    
    /**
     * Gets the rider's ID
     * 
     * @return The rider's ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets the rider's full name
     * 
     * @return The rider's full name
     */
    public String getFullName() {
        return fullName;
    }
    
    /**
     * Gets the rider's email or mobile number
     * 
     * @return The rider's email or mobile number
     */
    public String getEmailOrMobile() {
        return emailOrMobile;
    }
    
    /**
     * Gets the rider's vehicle type
     * 
     * @return The rider's vehicle type
     */
    public String getVehicleType() {
        return vehicleType;
    }
    
    /**
     * Gets the rider's license number
     * 
     * @return The rider's license number
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    /**
     * Gets the rider's vehicle registration number
     * 
     * @return The rider's vehicle registration number
     */
    public String getVehicleRegistration() {
        return vehicleRegistration;
    }
} 