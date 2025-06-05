/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nepxpress.database;

import java.sql.Connection;

/**
 *
 * @author Asus
 */
public class MySqlConnection {
    
    public void useConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Use the connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
