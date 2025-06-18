package nepxpress.test;

import nepxpress.view.Dashboard;

public class DashboardTest {
    public static void main(String[] args) {
        // Create and show dashboard
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                System.out.println("Dashboard opened successfully");
            } catch (Exception e) {
                System.err.println("Error opening dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
} 