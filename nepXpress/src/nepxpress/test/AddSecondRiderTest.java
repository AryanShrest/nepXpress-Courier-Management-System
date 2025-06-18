package nepxpress.test;

import nepxpress.database.RiderDAO;

public class AddSecondRiderTest {
    public static void main(String[] args) {
        // Second default rider details
        String fullName = "Suman Tamang";
        String emailOrMobile = "sumantamang123@gmail.com";
        String vehicleType = "Yamaha MT-15";
        String licenseNumber = "L456123";
        String vehicleRegistration = "BA-78-5432";
        String password = "password123";
        
        System.out.println("Registering second default rider:");
        System.out.println("Name: " + fullName);
        System.out.println("Email: " + emailOrMobile);
        System.out.println("Vehicle: " + vehicleType);
        System.out.println("License: " + licenseNumber);
        System.out.println("Registration: " + vehicleRegistration);
        
        // Register rider
        RiderDAO riderDAO = new RiderDAO();
        boolean success = riderDAO.createRider(
            fullName, 
            emailOrMobile,
            vehicleType, 
            licenseNumber, 
            vehicleRegistration, 
            password
        );
        
        System.out.println("Registration success: " + success);
        
        if (success) {
            System.out.println("\nSecond rider has been added to the database with Inactive status.");
            System.out.println("This rider will appear in the admin dashboard's Rider Approvals panel.");
        }
    }
} 