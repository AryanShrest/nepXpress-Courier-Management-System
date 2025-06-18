package nepxpress.database;

public class DatabaseConfig {
    // Database connection constants
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/nepxpress?allowMultiQueries=true";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "97012";
    
    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_RIDERS = "riders";
    public static final String TABLE_PASSWORD_RESETS = "password_resets";
    public static final String TABLE_USER_SESSIONS = "user_sessions";
    public static final String TABLE_DELIVERIES = "deliveries";
    
    // Database creation queries
    public static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS nepxpress";
    
    // Table creation queries
    public static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
        "id INT PRIMARY KEY AUTO_INCREMENT," +
        "first_name VARCHAR(50) NOT NULL," +
        "surname VARCHAR(50) NOT NULL," +
        "email_or_mobile VARCHAR(100) UNIQUE NOT NULL," +
        "password VARCHAR(255) NOT NULL," +
        "date_of_birth DATE NOT NULL," +
        "gender ENUM('Female', 'Male', 'Custom') NOT NULL," +
        "account_type ENUM('User', 'Rider') NOT NULL," +
        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
        ")";
    
    public static final String CREATE_RIDERS_TABLE = "CREATE TABLE IF NOT EXISTS riders (" +
        "id INT PRIMARY KEY AUTO_INCREMENT," +
        "user_id INT NOT NULL," +
        "vehicle_type VARCHAR(50) NOT NULL," +
        "license_number VARCHAR(50) UNIQUE NOT NULL," +
        "vehicle_registration VARCHAR(50) UNIQUE NOT NULL," +
        "status ENUM('Active', 'Inactive', 'Suspended') DEFAULT 'Inactive'," +
        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
        ")";
    
    public static final String CREATE_PASSWORD_RESETS_TABLE = "CREATE TABLE IF NOT EXISTS password_resets (" +
        "id INT PRIMARY KEY AUTO_INCREMENT," +
        "user_id INT NOT NULL," +
        "token VARCHAR(100) UNIQUE NOT NULL," +
        "used BOOLEAN DEFAULT FALSE," +
        "expires_at TIMESTAMP NOT NULL," +
        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
        ")";

    public static final String CREATE_USER_SESSIONS_TABLE = "CREATE TABLE IF NOT EXISTS user_sessions (" +
        "id INT PRIMARY KEY AUTO_INCREMENT," +
        "user_id INT NOT NULL," +
        "session_token VARCHAR(255) UNIQUE NOT NULL," +
        "ip_address VARCHAR(45)," +
        "user_agent VARCHAR(255)," +
        "last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "expires_at TIMESTAMP NOT NULL," +
        "is_active BOOLEAN DEFAULT TRUE," +
        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
        "INDEX idx_session_token (session_token)," +
        "INDEX idx_user_sessions (user_id, is_active)" +
        ")";

    public static final String CREATE_DELIVERIES_TABLE = "CREATE TABLE IF NOT EXISTS deliveries (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "rider_id INT NOT NULL," +
        "parcel_id VARCHAR(50) NOT NULL," +
        "delivery_status VARCHAR(50) NOT NULL," +
        "delivered_at DATETIME," +
        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "FOREIGN KEY (rider_id) REFERENCES riders(id) ON DELETE CASCADE" +
        ")";

    public static final String CREATE_PICKUPS_TABLE = "CREATE TABLE IF NOT EXISTS pickups (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "rider_id INT NOT NULL," +
        "pickup_address VARCHAR(255) NOT NULL," +
        "pickup_status VARCHAR(50) NOT NULL," +
        "scheduled_at DATETIME," +
        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "FOREIGN KEY (rider_id) REFERENCES riders(id) ON DELETE CASCADE" +
        ")";

    public static final String CREATE_RIDER_ISSUES_TABLE = "CREATE TABLE IF NOT EXISTS rider_issues (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "rider_id INT NOT NULL," +
        "issue_title VARCHAR(100) NOT NULL," +
        "issue_description TEXT NOT NULL," +
        "status VARCHAR(50) DEFAULT 'Open'," +
        "reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "FOREIGN KEY (rider_id) REFERENCES riders(id) ON DELETE CASCADE" +
        ")";

    public static final String CREATE_BRANCHES_TABLE = "CREATE TABLE IF NOT EXISTS branches (" +
        "id INT PRIMARY KEY AUTO_INCREMENT," +
        "branch_name VARCHAR(100) NOT NULL" +
        ")";

    public static final String CREATE_STAFF_TABLE = "CREATE TABLE IF NOT EXISTS staff (" +
        "id INT PRIMARY KEY AUTO_INCREMENT," +
        "full_name VARCHAR(100) NOT NULL," +
        "email VARCHAR(100) NOT NULL," +
        "branch_id INT," +
        "FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE SET NULL" +
        ")";

    // Sample data insertion queries
    public static final String INSERT_SAMPLE_DATA =
        "INSERT INTO users (first_name, surname, email_or_mobile, password, date_of_birth, gender, account_type) VALUES " +
        "('John', 'Doe', '9779841000001', 'password123', '1990-01-15', 'Male', 'User')," +
        "('Jane', 'Smith', '9779841000002', 'password456', '1992-03-20', 'Female', 'User')," +
        "('Ram', 'Kumar', '9779841000003', 'password789', '1988-07-10', 'Male', 'Rider')," +
        "('Sita', 'Sharma', '9779841000004', 'passwordabc', '1995-12-05', 'Female', 'Rider')," +
        "('Default', 'Rider', '9708059994', '123456798', '1990-01-01', 'Male', 'Rider');" +
        "INSERT INTO riders (user_id, vehicle_type, license_number, vehicle_registration, status) VALUES " +
        "(3, 'Motorcycle', 'L123456', 'BA-1-2345', 'Inactive')," +
        "(4, 'Scooter', 'L789012', 'BA-2-3456', 'Active')," +
        "(5, 'Motorcycle', 'L999999', 'BA-3-9999', 'Active');" +
        "INSERT INTO password_resets (user_id, token, used, expires_at) VALUES " +
        "(1, 'reset123', FALSE, DATE_ADD(NOW(), INTERVAL 1 HOUR));" +
        "INSERT INTO user_sessions (user_id, session_token, ip_address, user_agent, expires_at) VALUES " +
        "(1, 'session123', '127.0.0.1', 'Mozilla/5.0', DATE_ADD(NOW(), INTERVAL 24 HOUR));";

    public static final String INSERT_SAMPLE_RIDER_DATA =
        // Sample deliveries
        "INSERT INTO deliveries (rider_id, parcel_id, delivery_status, delivered_at) VALUES " +
        "(3, 'PCL001', 'Delivered', '2024-06-01 10:00:00')," +
        "(3, 'PCL002', 'In Transit', NULL)," +
        "(4, 'PCL003', 'Delivered', '2024-06-02 15:30:00');" +
        // Sample pickups
        "INSERT INTO pickups (rider_id, pickup_address, pickup_status, scheduled_at) VALUES " +
        "(3, 'Kathmandu, Nepal', 'Completed', '2024-06-01 08:00:00')," +
        "(4, 'Lalitpur, Nepal', 'Pending', '2024-06-03 09:00:00');" +
        // Sample rider issues
        "INSERT INTO rider_issues (rider_id, issue_title, issue_description, status) VALUES " +
        "(3, 'Late Delivery', 'Parcel was delivered late due to traffic.', 'Closed')," +
        "(4, 'Damaged Parcel', 'Received parcel was damaged.', 'Open');";
} 