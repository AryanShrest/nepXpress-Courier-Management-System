package nepxpress.test;

import nepxpress.database.RiderDAO;
import nepxpress.database.RiderInfo;
import java.util.List;

public class RiderRegistrationTest {
    public static void main(String[] args) {
        testRiderRegistration();
        testGetPendingRiders();
    }
    
    private static void testRiderRegistration() {
        System.out.println("--- Testing Rider Registration ---");
        
        RiderDAO riderDAO = new RiderDAO();
        boolean success = riderDAO.createRider(
            "Test Rider", 
            "testrider@example.com",
            "Honda CBR", 
            "LIC123456", 
            "REG789012", 
            "password123"
        );
        
        System.out.println("Registration success: " + success);
    }
    
    private static void testGetPendingRiders() {
        System.out.println("--- Testing Get Pending Riders ---");
        
        RiderDAO riderDAO = new RiderDAO();
        List<RiderInfo> pendingRiders = riderDAO.getPendingRiders();
        
        System.out.println("Number of pending riders: " + pendingRiders.size());
        
        for (RiderInfo rider : pendingRiders) {
            System.out.println("Rider ID: " + rider.getId());
            System.out.println("Name: " + rider.getFullName());
            System.out.println("Contact: " + rider.getEmailOrMobile());
            System.out.println("Vehicle: " + rider.getVehicleType());
            System.out.println("License: " + rider.getLicenseNumber());
            System.out.println("Registration: " + rider.getVehicleRegistration());
            System.out.println("-----------------------------");
        }
    }
} 