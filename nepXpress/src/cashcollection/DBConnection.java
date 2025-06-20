package cashcollection;

import java.sql.Connection;

public class DBConnection {
    public static Connection getConnection() {
        String url = "jdbc:mysql://127.0.0.1:3306/nepxpress"; // Replace 'nepxpress' with your DB name if different
        String user = "root";
        String pass = "97012";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return java.sql.DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | java.sql.SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createDeliveriesTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS deliveries ("
            + "parcel_id INT AUTO_INCREMENT PRIMARY KEY,"
            + "user_id INT,"
            + "customer_name VARCHAR(100),"
            + "address VARCHAR(255),"
            + "status VARCHAR(50),"
            + "amount DECIMAL(10,2),"
            + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY (user_id) REFERENCES users(id)"
            + ")";
        try (Connection conn = getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
