import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Test connection
            String url = "jdbc:mysql://localhost:3306/cms";
            String user = "root";
            String password = "";
            
            Connection conn = DriverManager.getConnection(url, user, password);
            
            if (conn != null) {
                JOptionPane.showMessageDialog(null, 
                    "Database connection successful!\n" +
                    "URL: " + url + "\n" +
                    "User: " + user,
                    "Connection Test",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Test if tables exist
                try {
                    conn.createStatement().executeQuery("SELECT * FROM users LIMIT 1");
                    conn.createStatement().executeQuery("SELECT * FROM parcels LIMIT 1");
                    conn.createStatement().executeQuery("SELECT * FROM complaints LIMIT 1");
                    
                    JOptionPane.showMessageDialog(null,
                        "All required tables exist!",
                        "Table Test",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,
                        "Table test failed: " + e.getMessage() + "\n" +
                        "Please make sure you have executed the database setup script.",
                        "Table Test Failed",
                        JOptionPane.ERROR_MESSAGE);
                }
                
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "MySQL JDBC Driver not found!\n" +
                "Please make sure you have added the MySQL Connector/J to your project.",
                "Driver Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Database connection failed!\n" +
                "Error: " + e.getMessage() + "\n\n" +
                "Please check:\n" +
                "1. XAMPP is running (MySQL service)\n" +
                "2. Database 'cms' exists\n" +
                "3. Username and password are correct",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 