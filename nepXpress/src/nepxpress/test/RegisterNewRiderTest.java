package nepxpress.test;

import nepxpress.database.RiderDAO;
import java.util.Random;

public class RegisterNewRiderTest {
    public static void main(String[] args) {
        // Generate a random suffix to ensure unique values
        String randomSuffix = String.valueOf(new Random().nextInt(10000));
        
        // Create rider details
        String fullName = "Test Rider " + randomSuffix;
        String emailOrMobile = "testrider" + randomSuffix + "@example.com";
        String vehicleType = "Honda CBR" + randomSuffix;
        String licenseNumber = "LIC" + randomSuffix;
        String vehicleRegistration = "REG" + randomSuffix;
        String password = "password123";
        
        System.out.println("Registering new rider:");
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
            System.out.println("\nNow run the QueryRidersTest to verify the new rider is in the database:");
            System.out.println("java -cp \".;../lib/mysql-connector-j-8.0.33.jar\" nepxpress.test.QueryRidersTest");
        }
    }
} 