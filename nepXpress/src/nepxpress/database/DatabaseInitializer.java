package nepxpress.database;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Class for initializing the database
 */
public class DatabaseInitializer {
    
    /**
     * Initialize the database by creating tables if they don't exist
     * 
     * @return true if initialization was successful, false otherwise
     */
    public static boolean initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Initializing database...");
            
            // Create tables in the correct order (respecting foreign key constraints)
            for (String sql : DatabaseConfig.TABLE_CREATION_SQL) {
                stmt.executeUpdate(sql);
            }
            
            System.out.println("Database initialized successfully.");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 