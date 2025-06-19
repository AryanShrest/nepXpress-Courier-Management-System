package com.courier.util;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class XAMPPDatabaseTest {
    public static void main(String[] args) {
        try {
            // Test database connection
            System.out.println("Attempting to connect to XAMPP MySQL...");
            Connection conn = DatabaseUtil.getConnection();
            
            if (conn != null) {
                System.out.println("Successfully connected to XAMPP MySQL!");
                
                // Test if database exists
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'cms'");
                    if (rs.next()) {
                        System.out.println("Database 'cms' exists!");
                        
                        // Test if tables exist
                        rs = stmt.executeQuery("SHOW TABLES FROM cms");
                        System.out.println("\nExisting tables in 'cms' database:");
                        while (rs.next()) {
                            System.out.println("- " + rs.getString(1));
                        }
                    } else {
                        System.out.println("Database 'cms' does not exist. Please create it using phpMyAdmin.");
                    }
                }
                
                DatabaseUtil.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "XAMPP MySQL connection failed: " + e.getMessage() + "\n\n" +
                "Please make sure:\n" +
                "1. XAMPP is running (Apache and MySQL)\n" +
                "2. Database 'cms' exists in phpMyAdmin\n" +
                "3. MySQL JDBC driver is in your project's classpath",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 