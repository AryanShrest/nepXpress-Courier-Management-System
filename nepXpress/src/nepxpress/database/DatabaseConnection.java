package nepxpress.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static Connection connection = null;
    
    static {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver registered.");
            
            // Create initial connection to MySQL server (not the database)
            connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306",
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );
            System.out.println("Connected to MySQL server.");
            
            // Initialize database and tables
            createDatabaseAndTables();
            
            // Close the initial connection and create a new one to the specific database
            connection.close();
            connection = DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );
            System.out.println("Connected to nepxpress database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }
    
    private static void createDatabaseAndTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create database if not exists
            System.out.println("Creating database...");
            stmt.execute(DatabaseConfig.CREATE_DATABASE);
            stmt.execute("USE nepxpress");
            
            // First, check existing tables
            System.out.println("\nChecking existing tables...");
            List<String> existingTables = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                System.out.println("Current tables in database:");
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    existingTables.add(tableName);
                    System.out.println("- " + tableName);
                }
            }
            
            // Drop existing tables if they exist (in reverse order of dependencies)
            System.out.println("\nDropping existing tables...");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0"); // Temporarily disable foreign key checks
            
            // Drop tables in reverse dependency order
            String[] tablesToDrop = {
                "user_sessions",
                "password_resets",
                "riders",
                "users",
                "deliveries",  // Drop old tables too
                "orders"
            };
            
            for (String table : tablesToDrop) {
                try {
                    System.out.println("Dropping table: " + table);
                    stmt.execute("DROP TABLE IF EXISTS " + table);
                } catch (SQLException e) {
                    System.err.println("Error dropping table " + table + ": " + e.getMessage());
                }
            }
            
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1"); // Re-enable foreign key checks
            
            // Create tables in order of dependencies
            System.out.println("\nCreating new tables:");
            
            // 1. Create users table (no dependencies)
            System.out.println("1. Creating users table...");
            stmt.execute(DatabaseConfig.CREATE_USERS_TABLE);
            verifyTableCreation(stmt, "users");
            
            // 2. Create riders table (depends on users)
            System.out.println("2. Creating riders table...");
            stmt.execute(DatabaseConfig.CREATE_RIDERS_TABLE);
            verifyTableCreation(stmt, "riders");
            
            // 3. Create password_resets table (depends on users)
            System.out.println("3. Creating password_resets table...");
            stmt.execute(DatabaseConfig.CREATE_PASSWORD_RESETS_TABLE);
            verifyTableCreation(stmt, "password_resets");
            
            // 4. Create user_sessions table (depends on users)
            System.out.println("4. Creating user_sessions table...");
            stmt.execute(DatabaseConfig.CREATE_USER_SESSIONS_TABLE);
            verifyTableCreation(stmt, "user_sessions");
            
            // Final verification
            System.out.println("\nFinal table verification:");
            List<String> tables = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                while (rs.next()) {
                    tables.add(rs.getString(1));
                }
            }
            
            // Now describe each table
            for (String tableName : tables) {
                System.out.println("\nTable: " + tableName);
                try (ResultSet desc = stmt.executeQuery("DESCRIBE " + tableName)) {
                    while (desc.next()) {
                        System.out.println("  - " + desc.getString("Field") + " " + 
                                        desc.getString("Type") + " " + 
                                        desc.getString("Null") + " " + 
                                        desc.getString("Key"));
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating database and tables: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create database and tables", e);
        }
    }
    
    private static void verifyTableCreation(Statement stmt, String tableName) throws SQLException {
        try (ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE '" + tableName + "'")) {
            if (rs.next()) {
                System.out.println("  ✓ Table '" + tableName + "' created successfully");
            } else {
                throw new SQLException("Failed to create table: " + tableName);
            }
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} 