package nepxpress.test;

import javax.swing.SwingUtilities;
import nepxpress.view.RiderSignupView;

/**
 * Simple test for the RiderSignupView
 */
public class SimpleRiderSignupTest {
    
    public static void main(String[] args) {
        System.out.println("=== Simple Rider Signup Test ===");
        System.out.println("Launching the rider signup form...");
        
        SwingUtilities.invokeLater(() -> {
            RiderSignupView view = new RiderSignupView();
            view.setVisible(true);
            
            System.out.println("Rider signup form launched.");
            System.out.println("Please fill out the form and click Submit.");
            System.out.println("Check the console for debug output.");
        });
    }
} 