package nepxpress.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    public static void main(String[] args) {
        System.out.println("DatabaseConnection initialized. If you see this message, DB setup ran successfully.");
    }
    private static Connection connection = null;
    private static boolean isInitialized = false;
    
    static {
        try {
            System.out.println("Attempting to initialize database connection and tables...");
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create connection to MySQL server and select database
            connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/nepxpress?user=root&password=97012"
            );
            
            // Initialize database and tables only once
            if (!isInitialized) {
                initializeDatabase();
                isInitialized = true;
            }
            
            System.out.println("Database and tables initialized successfully.");
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }
    
    private static void initializeDatabase() {
        System.out.println("Executing initializeDatabase()...");
        try (Statement stmt = connection.createStatement()) {
            // Create database if not exists
            stmt.execute(DatabaseConfig.CREATE_DATABASE);
            stmt.execute("USE nepxpress");
            
            // First, check existing tables
            List<String> existingTables = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    existingTables.add(tableName);
                }
            }
            System.out.println("Existing tables: " + existingTables);
            
            // Create tables only if they don't exist
            if (!existingTables.contains("users")) {
                System.out.println("Creating tables...");
                // 1. Create users table (no dependencies)
                stmt.execute(DatabaseConfig.CREATE_USERS_TABLE);
                verifyTableCreation(stmt, "users");
                System.out.println("Table 'users' created.");
            }
            
            if (!existingTables.contains("riders")) {
                // 2. Create riders table (depends on users)
                stmt.execute(DatabaseConfig.CREATE_RIDERS_TABLE);
                verifyTableCreation(stmt, "riders");
                System.out.println("Table 'riders' created.");
            }
            
            if (!existingTables.contains("password_resets")) {
                // 3. Create password_resets table (depends on users)
                stmt.execute(DatabaseConfig.CREATE_PASSWORD_RESETS_TABLE);
                verifyTableCreation(stmt, "password_resets");
                System.out.println("Table 'password_resets' created.");
            }
            
            if (!existingTables.contains("user_sessions")) {
                // 4. Create user_sessions table (depends on users)
                stmt.execute(DatabaseConfig.CREATE_USER_SESSIONS_TABLE);
                verifyTableCreation(stmt, "user_sessions");
                System.out.println("Table 'user_sessions' created.");
            }

            if (!existingTables.contains("deliveries")) {
                // 5. Create deliveries table (depends on users)
                stmt.execute(DatabaseConfig.CREATE_DELIVERIES_TABLE);
                verifyTableCreation(stmt, "deliveries");
                System.out.println("Table 'deliveries' created.");
            }

            if (!existingTables.contains("pickups")) {
                stmt.execute(DatabaseConfig.CREATE_PICKUPS_TABLE);
                verifyTableCreation(stmt, "pickups");
                System.out.println("Table 'pickups' created.");
            }

            if (!existingTables.contains("rider_issues")) {
                stmt.execute(DatabaseConfig.CREATE_RIDER_ISSUES_TABLE);
                verifyTableCreation(stmt, "rider_issues");
                System.out.println("Table 'rider_issues' created.");
            }
            
            if (!existingTables.contains("branches")) {
                // 6. Create branches table (no dependencies)
                stmt.execute(DatabaseConfig.CREATE_BRANCHES_TABLE);
                verifyTableCreation(stmt, "branches");
                System.out.println("Table 'branches' created.");
            }
            
            if (!existingTables.contains("staff")) {
                // 7. Create staff table (depends on branches and users)
                stmt.execute(DatabaseConfig.CREATE_STAFF_TABLE);
                verifyTableCreation(stmt, "staff");
                System.out.println("Table 'staff' created.");
            }
            
            if (!existingTables.contains("complaints")) {
                stmt.execute(DatabaseConfig.CREATE_COMPLAINTS_TABLE);
                verifyTableCreation(stmt, "complaints");
                System.out.println("Table 'complaints' created.");
            }
            if (!existingTables.contains("contact_messages")) {
                stmt.execute(DatabaseConfig.CREATE_CONTACT_MESSAGES_TABLE);
                verifyTableCreation(stmt, "contact_messages");
                System.out.println("Table 'contact_messages' created.");
            }
            
            if (!existingTables.contains("deactivated_accounts")) {
                stmt.execute(DatabaseConfig.CREATE_DEACTIVATED_ACCOUNTS_TABLE);
                verifyTableCreation(stmt, "deactivated_accounts");
                System.out.println("Table 'deactivated_accounts' created.");
            }
            
            // Insert sample data only if users table is empty
            boolean hasUsers = false;
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next()) {
                    hasUsers = rs.getInt(1) > 0;
                }
            }
            
            if (!hasUsers) {
                System.out.println("Inserting sample data...");
                String[] sqlStatements = DatabaseConfig.INSERT_SAMPLE_DATA.split(";");
                for (String sql : sqlStatements) {
                    String trimmedSql = sql.trim();
                    if (!trimmedSql.isEmpty() && !trimmedSql.startsWith("--")) {
                        try {
                            stmt.execute(trimmedSql);
                        } catch (SQLException e) {
                            // Ignore duplicate errors for sample data
                        }
                    }
                }
                // Ensure default admin always exists
                try {
                    String insertUserSql = "INSERT INTO users (first_name, surname, email_or_mobile, password, date_of_birth, gender, account_type) VALUES ('Default', 'Admin', '9708096796', '12345678', '1990-01-01', 'Custom', 'Admin')";
                    stmt.execute(insertUserSql);
                    System.out.println("Default admin created: 9708096796/12345678");
                } catch (SQLException e) {
                    System.err.println("Admin user may already exist: " + e.getMessage());
                }
                System.out.println("Sample data inserted successfully.");
            }

            // Insert sample data for deliveries, pickups, and rider_issues if empty
            boolean hasDeliveries = false;
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM deliveries")) {
                if (rs.next()) {
                    hasDeliveries = rs.getInt(1) > 0;
                }
            }
            if (!hasDeliveries) {
                System.out.println("Inserting sample deliveries, pickups, and rider issues...");
                String[] sqlStatements = DatabaseConfig.INSERT_SAMPLE_RIDER_DATA.split(";");
                for (String sql : sqlStatements) {
                    String trimmedSql = sql.trim();
                    if (!trimmedSql.isEmpty() && !trimmedSql.startsWith("--")) {
                        try {
                            stmt.execute(trimmedSql);
                        } catch (SQLException e) {
                            // Ignore duplicate errors for sample data
                        }
                    }
                }
                System.out.println("Sample rider data inserted successfully.");
            }

            // DEBUG: Print all users in the database
            printAllUsers(stmt);
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    // Original method kept for backward compatibility, but not used anymore
    private static void createDatabaseAndTables() {
        System.out.println("WARNING: createDatabaseAndTables() called but skipped to prevent data loss");
        // Method intentionally left empty to prevent table dropping
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
                "jdbc:mysql://127.0.0.1:3306/nepxpress?user=root&password=97012"
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
    
    // DEBUG: Print all users in the database
    private static void printAllUsers(Statement stmt) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            System.out.println("--- USERS TABLE ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email_or_mobile");
                String password = rs.getString("password");
                String type = rs.getString("account_type");
                System.out.println("id=" + id + ", mobile=" + email + ", password=" + password + ", type=" + type);
            }
            System.out.println("-------------------");
        } catch (SQLException e) {
            System.err.println("Error printing users: " + e.getMessage());
        }
    }
}