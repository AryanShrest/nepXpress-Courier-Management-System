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
            
            // Create initial connection to MySQL server (not the database)
            connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306",
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );
            
            // Initialize database and tables
            createDatabaseAndTables();
            
            // Close the initial connection and create a new one to the specific database
            connection.close();
            connection = DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }
    
    private static void createDatabaseAndTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create database if not exists
            stmt.execute(DatabaseConfig.CREATE_DATABASE);
            stmt.execute("USE nepxpress");
            
            // Create tables in order of dependencies (only if they don't exist)
            
            // 1. Create users table (no dependencies)
            stmt.execute(DatabaseConfig.CREATE_USERS_TABLE);
            verifyTableCreation(stmt, "users");
            
            // 2. Create riders table (depends on users)
            stmt.execute(DatabaseConfig.CREATE_RIDERS_TABLE);
            verifyTableCreation(stmt, "riders");
            
            // 3. Create password_resets table (depends on users)
            stmt.execute(DatabaseConfig.CREATE_PASSWORD_RESETS_TABLE);
            verifyTableCreation(stmt, "password_resets");
            
            // 4. Create user_sessions table (depends on users)
            stmt.execute(DatabaseConfig.CREATE_USER_SESSIONS_TABLE);
            verifyTableCreation(stmt, "user_sessions");
            
            // Final verification
            List<String> tables = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                while (rs.next()) {
                    tables.add(rs.getString(1));
                }
            }
            
            // Now describe each table
            for (String tableName : tables) {
                try (ResultSet desc = stmt.executeQuery("DESCRIBE " + tableName)) {
                    while (desc.next()) {
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