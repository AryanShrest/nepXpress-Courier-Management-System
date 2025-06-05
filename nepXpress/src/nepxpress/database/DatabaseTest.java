package nepxpress.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseTest {
    public static void testConnection() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                JOptionPane.showMessageDialog(null,
                    "Database connection successful!\n" +
                    "Connected to: " + conn.getMetaData().getURL() + "\n" +
                    "Database product: " + conn.getMetaData().getDatabaseProductName() + " " + 
                    conn.getMetaData().getDatabaseProductVersion(),
                    "Connection Test",
                    JOptionPane.INFORMATION_MESSAGE);
                conn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Database connection failed!\n\nError: " + e.getMessage() + 
                "\n\nPlease check if:\n" +
                "1. MySQL Server is running\n" +
                "2. Credentials are correct\n" +
                "3. Port 3306 is accessible",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
} 