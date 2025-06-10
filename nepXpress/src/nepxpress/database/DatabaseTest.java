package nepxpress.database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void testConnection() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 