package com.courier.util;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            // Test database connection
            Connection conn = DatabaseUtil.getConnection();
            if (conn != null) {
                System.out.println("Database connection successful!");
                
                // Test creating a user
                String insertUser = """
                    INSERT INTO users (username, password, email, first_name, last_name, role)
                    VALUES ('testuser', 'password123', 'test@example.com', 'Test', 'User', 'customer')
                    """;
                
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(insertUser);
                    System.out.println("Test user created successfully!");
                    
                    // Test querying the user
                    ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = 'testuser'");
                    if (rs.next()) {
                        System.out.println("Found user: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    }
                }
                
                DatabaseUtil.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Database connection failed: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 