package nepxpress.test;

import javax.swing.SwingUtilities;
import nepxpress.view.RiderSignupView;

/**
 * Test class to verify that the rider registration form works with the password field
 */
public class RiderSignupWithPasswordTest {
    
    public static void main(String[] args) {
        System.out.println("=== Rider Signup With Password Test ===");
        System.out.println("Launching the rider signup form with password field...");
        
        SwingUtilities.invokeLater(() -> {
            RiderSignupView view = new RiderSignupView();
            view.setVisible(true);
            
            System.out.println("Rider signup form launched.");
            System.out.println("Please fill out the form including the password field and click Submit.");
            System.out.println("Instructions:");
            System.out.println("1. Fill in all required fields including the password");
            System.out.println("2. Click Submit");
            System.out.println("3. After successful registration, you should be able to login with the email and password");
        });
    }
} 