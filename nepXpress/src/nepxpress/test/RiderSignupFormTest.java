package nepxpress.test;

import javax.swing.SwingUtilities;
import nepxpress.view.RiderSignupView;

/**
 * Test class to verify that the rider signup form works correctly
 */
public class RiderSignupFormTest {
    
    public static void main(String[] args) {
        // Launch the rider signup form
        SwingUtilities.invokeLater(() -> {
            RiderSignupView view = new RiderSignupView();
            view.setVisible(true);
            
            System.out.println("Rider signup form launched.");
            System.out.println("Fill in the form and click Submit to test the rider registration.");
            System.out.println("After submitting, check the admin dashboard to verify that the rider appears in the approval list.");
        });
    }
} 