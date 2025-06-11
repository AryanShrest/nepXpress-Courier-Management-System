package nepxpress.database;

/**
 * Class to hold rider information including both rider and user details
 */
public class RiderInfo {
    private final int id;
    private final int userId;
    private final String firstName;
    private final String surname;
    private final String emailOrMobile;
    private final String vehicleType;
    private final String licenseNumber;
    private final String vehicleRegistration;
    private final String status;
    
    public RiderInfo(int id, int userId, String firstName, String surname, 
                    String emailOrMobile, String vehicleType, String licenseNumber, 
                    String vehicleRegistration, String status) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.surname = surname;
        this.emailOrMobile = emailOrMobile;
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
        this.vehicleRegistration = vehicleRegistration;
        this.status = status;
    }
    
    public int getId() {
        return id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public String getFullName() {
        return firstName + " " + surname;
    }
    
    public String getEmailOrMobile() {
        return emailOrMobile;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public String getVehicleRegistration() {
        return vehicleRegistration;
    }
    
    public String getStatus() {
        return status;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s (%s - %s)", 
            firstName, surname, vehicleType, vehicleRegistration);
    }
} 