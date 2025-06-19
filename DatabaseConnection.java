import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cms?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load the JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Create the connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                
                // Test the connection
                if (!connection.isValid(5)) {
                    throw new SQLException("Database connection is not valid");
                }
            }
            return connection;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver not found!\n" +
                "Please make sure you have added the MySQL Connector/J to your project.",
                "Driver Error",
                JOptionPane.ERROR_MESSAGE);
            return null;
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
            return null;
        }
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null && conn != connection) {
            try {
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, 
                    "Error closing connection: " + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void closeAllConnections() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, 
                    "Error closing main connection: " + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static boolean testConnection() {
        Connection testConn = getConnection();
        if (testConn != null) {
            try {
                return testConn.isValid(5);
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }
    
    public static void executeQuery(String query) throws SQLException {
        Connection conn = getConnection();
        if (conn != null) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.execute();
            }
        }
    }
    
    public static ResultSet executeQueryWithResult(String query) throws SQLException {
        Connection conn = getConnection();
        if (conn != null) {
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        }
        return null;
    }
} 