package nepxpress.view;

import javax.swing.SwingUtilities;

public class RunDashboard {
    public static void main(String[] args) {
        System.out.println("Starting application...");
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    DashboardFrame frame = new DashboardFrame();
                    frame.setVisible(true);
                    System.out.println("Frame should be visible now");
                } catch (Exception e) {
                    System.err.println("Error creating frame: " + e);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error in main: " + e);
            e.printStackTrace();
        }
    }
} 