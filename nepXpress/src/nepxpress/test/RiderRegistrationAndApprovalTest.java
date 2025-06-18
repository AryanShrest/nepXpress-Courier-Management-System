package nepxpress.test;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import nepxpress.database.RiderDAO;
import nepxpress.model.RiderInfo;
import nepxpress.view.Dashboard;

/**
 * Test class to verify rider registration and approval flow
 */
public class RiderRegistrationAndApprovalTest {
    
    public static void main(String[] args) {
        // First register a test rider
        registerTestRider();
        
        // Then open the admin dashboard to see and approve/reject the rider
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
            JOptionPane.showMessageDialog(dashboard, 
                "Test rider registered. Please check the Rider Approvals section to see the pending rider.", 
                "Test Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private static void registerTestRider() {
        System.out.println("=== Registering Test Rider ===");
        
        RiderDAO riderDAO = new RiderDAO();
        
        // Delete existing test riders with the same email (for clean testing)
        // Note: this would require implementing a method to delete riders by email
        // For now, we'll just create a new rider with a timestamp in the email
        
        String timestamp = String.valueOf(System.currentTimeMillis());
        String testEmail = "testrider" + timestamp + "@example.com";
        
        // Register a new rider
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
        } else {
            System.err.println("Rider registration failed!");
            return;
        }
        
        // Verify the rider was added to the pending list
        List<RiderInfo> pendingRiders = riderDAO.getPendingRiders();
        
        System.out.println("Number of pending riders: " + pendingRiders.size());
        
        boolean found = false;
        for (RiderInfo rider : pendingRiders) {
            System.out.println("Checking rider: " + rider.getFullName() + " - " + rider.getContactInfo());
            if (rider.getContactInfo().equals(testEmail)) {
                found = true;
                System.out.println("Found the test rider in pending riders!");
                System.out.println("Rider ID: " + rider.getId());
                System.out.println("Name: " + rider.getFullName());
                System.out.println("Contact: " + rider.getContactInfo());
                System.out.println("Vehicle: " + rider.getVehicleType());
                System.out.println("License: " + rider.getLicenseNumber());
                System.out.println("Registration: " + rider.getVehicleRegistration());
                break;
            }
        }
        
        if (!found) {
            System.err.println("Test rider not found in pending riders list!");
        }
    }
} 