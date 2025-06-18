package nepxpress.test;

import nepxpress.database.RiderDAO;
import nepxpress.model.RiderInfo;
import java.util.List;

/**
 * Simple test for rider registration flow
 */
public class SimpleRiderRegistrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Simple Rider Registration Test ===");
        
        // Register a test rider
        registerTestRider();
        
        // Verify the rider was registered
        checkPendingRiders();
        
        System.out.println("\nTest completed. Now you can run the Dashboard to approve the rider.");
        System.out.println("To run the Dashboard, use: java -cp \"src;lib/*\" nepxpress.view.Dashboard");
    }
    
    private static void registerTestRider() {
        System.out.println("Registering test rider...");
        
        String timestamp = String.valueOf(System.currentTimeMillis());
        String testEmail = "testrider" + timestamp + "@example.com";
        
        RiderDAO riderDAO = new RiderDAO();
        boolean success = riderDAO.createRider(
            "Test Rider", 
            testEmail,
            "Honda CBR", 
            "LIC" + timestamp, 
            "REG" + timestamp, 
            "password123"
        );
        
        if (success) {
            System.out.println("Rider registration successful!");
            System.out.println("Email: " + testEmail);
        } else {
            System.err.println("Rider registration failed!");
        }
    }
    
    private static void checkPendingRiders() {
        System.out.println("\nChecking pending riders...");
        
        RiderDAO riderDAO = new RiderDAO();
        List<RiderInfo> pendingRiders = riderDAO.getPendingRiders();
        
        System.out.println("Number of pending riders: " + pendingRiders.size());
        
        if (!pendingRiders.isEmpty()) {
            System.out.println("\nListing pending riders:");
            for (RiderInfo rider : pendingRiders) {
                System.out.println("-----------------------------------");
                System.out.println("Rider ID: " + rider.getId());
                System.out.println("Name: " + rider.getFullName());
                System.out.println("Contact: " + rider.getContactInfo());
                System.out.println("Vehicle: " + rider.getVehicleType());
                System.out.println("License: " + rider.getLicenseNumber());
                System.out.println("Registration: " + rider.getVehicleRegistration());
            }
            System.out.println("-----------------------------------");
        } else {
            System.out.println("No pending riders found!");
        }
    }
} 