package nepxpress.test;

import nepxpress.database.RiderDAO;

public class AddDefaultRiderTest {
    public static void main(String[] args) {
        // Specific rider details requested by user
        String fullName = "Aaryan Shrestha";
        String emailOrMobile = "aryanshrestha0307@gmail.com";
        String vehicleType = "CBR 600";
        String licenseNumber = "L123789";
        String vehicleRegistration = "BA-56-7890";
        String password = "password123";
        
        System.out.println("Registering specific rider:");
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
            System.out.println("\nRider has been added to the database with Inactive status.");
            System.out.println("This rider will appear in the admin dashboard's Rider Approvals panel.");
        }
    }
} 