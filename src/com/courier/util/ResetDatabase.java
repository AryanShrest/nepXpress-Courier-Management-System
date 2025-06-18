package com.courier.util;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ResetDatabase {
    public static void main(String[] args) {
        try {
            // First, connect to MySQL without specifying a database
            String url = "jdbc:mysql://localhost:3306?useSSL=false";
            Connection conn = DriverManager.getConnection(url, "root", "");
            
            try (Statement stmt = conn.createStatement()) {
                // Drop database if exists
                stmt.executeUpdate("DROP DATABASE IF EXISTS cms");
                System.out.println("Dropped existing database if any");
                
                // Create new database
                stmt.executeUpdate("CREATE DATABASE cms");
                System.out.println("Created new database 'cms'");
                
                // Switch to the new database
                stmt.executeUpdate("USE cms");
                
                // Create users table
                stmt.executeUpdate("""
                    CREATE TABLE users (
                        user_id INT PRIMARY KEY AUTO_INCREMENT,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        role ENUM('admin', 'staff', 'customer') NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """);
                System.out.println("Created users table");
                
                // Create parcels table
                stmt.executeUpdate("""
                    CREATE TABLE parcels (
                        parcel_id INT PRIMARY KEY AUTO_INCREMENT,
                        tracking_number VARCHAR(20) UNIQUE NOT NULL,
                        sender_id INT NOT NULL,
                        receiver_name VARCHAR(100) NOT NULL,
                        receiver_phone VARCHAR(20) NOT NULL,
                        receiver_address TEXT NOT NULL,
                        weight DECIMAL(10,2) NOT NULL,
                        status ENUM('pending', 'in_transit', 'delivered', 'cancelled') NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (sender_id) REFERENCES users(user_id)
                    )
                """);
                System.out.println("Created parcels table");
                
                // Create complaints table
                stmt.executeUpdate("""
                    CREATE TABLE complaints (
                        complaint_id INT PRIMARY KEY AUTO_INCREMENT,
                        parcel_id INT NOT NULL,
                        user_id INT NOT NULL,
                        nature VARCHAR(100) NOT NULL,
                        description TEXT NOT NULL,
                        status ENUM('pending', 'in_progress', 'resolved') NOT NULL DEFAULT 'pending',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id),
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
                    )
                """);
                System.out.println("Created complaints table");
                
                // Create tracking history table
                stmt.executeUpdate("""
                    CREATE TABLE tracking_history (
                        history_id INT PRIMARY KEY AUTO_INCREMENT,
                        parcel_id INT NOT NULL,
                        status ENUM('pending', 'in_transit', 'delivered', 'cancelled') NOT NULL,
                        location VARCHAR(100) NOT NULL,
                        description TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id)
                    )
                """);
                System.out.println("Created tracking_history table");
                
                // Create dispatch table
                stmt.executeUpdate("""
                    CREATE TABLE dispatch (
                        dispatch_id INT PRIMARY KEY AUTO_INCREMENT,
                        parcel_id INT NOT NULL,
                        courier_id INT NOT NULL,
                        dispatch_date TIMESTAMP NOT NULL,
                        status ENUM('assigned', 'picked_up', 'in_transit', 'delivered') NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (parcel_id) REFERENCES parcels(parcel_id),
                        FOREIGN KEY (courier_id) REFERENCES users(user_id)
                    )
                """);
                System.out.println("Created dispatch table");
                
                // Insert sample admin user
                stmt.executeUpdate("""
                    INSERT INTO users (username, password, email, first_name, last_name, role)
                    VALUES ('admin', 'admin123', 'admin@cms.com', 'Admin', 'User', 'admin')
                """);
                System.out.println("Created sample admin user");
                
                JOptionPane.showMessageDialog(null,
                    "Database reset successful!\n\n" +
                    "Created new 'cms' database with all tables.\n" +
                    "Sample admin user created:\n" +
                    "Username: admin\n" +
                    "Password: admin123",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
            }
            
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error resetting database: " + e.getMessage() + "\n\n" +
                "Please make sure:\n" +
                "1. XAMPP is running\n" +
                "2. MySQL service is started\n" +
                "3. MySQL JDBC driver is in your classpath",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 