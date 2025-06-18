package nepxpress.test;

import nepxpress.dao.BranchDAO;
import nepxpress.database.RiderDAO;
import nepxpress.database.DatabaseConnection;
import nepxpress.database.DatabaseInitializer;
import nepxpress.model.Branch;
import nepxpress.model.RiderInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Test class for initializing rider-related database tables and inserting sample data
 */
public class RiderDatabaseTest {
    
    public static void main(String[] args) {
        // Initialize database schema
        boolean initialized = DatabaseInitializer.initializeDatabase();
        
        if (initialized) {
            System.out.println("Database initialized successfully!");
            
            // Insert sample data
            insertSampleRiders();
            insertSampleDeliveries();
            insertSamplePickups();
            insertSampleCashCollections();
            insertSampleIssues();
            
            System.out.println("Sample data inserted successfully!");
        } else {
            System.err.println("Database initialization failed!");
        }
    }
    
    private static void insertSampleRiders() {
        RiderDAO riderDAO = new RiderDAO();
        BranchDAO branchDAO = new BranchDAO();
        
        // Check if riders already exist
        if (riderDAO.getAllRiders().isEmpty()) {
            System.out.println("Inserting sample rider data...");
            
            // Get branches
            Branch branch1 = branchDAO.getBranchByCode("BR001");
            Branch branch2 = branchDAO.getBranchByCode("BR002");
            
            if (branch1 == null || branch2 == null) {
                System.out.println("Error: Branches not found. Please run InsertSampleDataTest first.");
                return;
            }
            
            // Create sample riders
            RiderInfo rider1 = new RiderInfo("RDR1234567", branch1.getId(), "Rajesh Kumar", 
                                          "rajesh.kumar@nepxpress.com", "Motorcycle", 
                                          "L123456", "BA-1-2345");
            
            RiderInfo rider2 = new RiderInfo("RDR2345678", branch1.getId(), "Sunil Thapa", 
                                          "sunil.thapa@nepxpress.com", "Scooter", 
                                          "L234567", "BA-2-3456");
            
            RiderInfo rider3 = new RiderInfo("RDR3456789", branch2.getId(), "Manoj Shrestha", 
                                          "manoj.shrestha@nepxpress.com", "Motorcycle", 
                                          "L345678", "BA-3-4567");
            
            // Insert riders
            int id1 = riderDAO.insertRider(rider1);
            int id2 = riderDAO.insertRider(rider2);
            int id3 = riderDAO.insertRider(rider3);
            
            System.out.println("Inserted riders with IDs: " + id1 + ", " + id2 + ", " + id3);
        } else {
            System.out.println("Riders already exist in the database.");
        }
    }
    
    private static void insertSampleDeliveries() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if deliveries already exist
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM deliveries");
            if (checkStmt.executeQuery().next() && checkStmt.executeQuery().getInt(1) > 0) {
                System.out.println("Deliveries already exist in the database.");
                return;
            }
            
            System.out.println("Inserting sample delivery data...");
            
            // Get rider IDs
            RiderDAO riderDAO = new RiderDAO();
            BranchDAO branchDAO = new BranchDAO();
            
            // Get sample riders and branches
            RiderInfo rider1 = riderDAO.getRiderByRiderId("RDR1234567");
            RiderInfo rider2 = riderDAO.getRiderByRiderId("RDR2345678");
            Branch branch1 = branchDAO.getBranchByCode("BR001");
            
            if (rider1 == null || rider2 == null || branch1 == null) {
                System.out.println("Error: Riders or branches not found.");
                return;
            }
            
            // Insert sample deliveries
            String sql = "INSERT INTO deliveries (delivery_id, rider_id, branch_id, customer_name, " +
                         "customer_contact, delivery_address, package_description, package_weight, " +
                         "delivery_fee, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            // Delivery 1
            pstmt.setString(1, "DEL" + System.currentTimeMillis());
            pstmt.setInt(2, rider1.getId());
            pstmt.setInt(3, branch1.getId());
            pstmt.setString(4, "Arun Sharma");
            pstmt.setString(5, "9801234567");
            pstmt.setString(6, "123 Main St, Kathmandu");
            pstmt.setString(7, "Electronics package");
            pstmt.setDouble(8, 2.5);
            pstmt.setDouble(9, 150.0);
            pstmt.setString(10, "Pending");
            pstmt.executeUpdate();
            
            // Delivery 2
            pstmt.setString(1, "DEL" + (System.currentTimeMillis() + 1));
            pstmt.setInt(2, rider2.getId());
            pstmt.setInt(3, branch1.getId());
            pstmt.setString(4, "Binita Rai");
            pstmt.setString(5, "9802345678");
            pstmt.setString(6, "456 Oak St, Lalitpur");
            pstmt.setString(7, "Clothing package");
            pstmt.setDouble(8, 1.2);
            pstmt.setDouble(9, 100.0);
            pstmt.setString(10, "Delivered");
            pstmt.executeUpdate();
            
            // Delivery 3
            pstmt.setString(1, "DEL" + (System.currentTimeMillis() + 2));
            pstmt.setInt(2, rider1.getId());
            pstmt.setInt(3, branch1.getId());
            pstmt.setString(4, "Chetan Gurung");
            pstmt.setString(5, "9803456789");
            pstmt.setString(6, "789 Pine St, Bhaktapur");
            pstmt.setString(7, "Food package");
            pstmt.setDouble(8, 0.8);
            pstmt.setDouble(9, 80.0);
            pstmt.setString(10, "In Transit");
            pstmt.executeUpdate();
            
            System.out.println("Sample deliveries inserted successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error inserting sample deliveries: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertSamplePickups() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if pickups already exist
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM pickups");
            if (checkStmt.executeQuery().next() && checkStmt.executeQuery().getInt(1) > 0) {
                System.out.println("Pickups already exist in the database.");
                return;
            }
            
            System.out.println("Inserting sample pickup data...");
            
            // Get rider IDs
            RiderDAO riderDAO = new RiderDAO();
            BranchDAO branchDAO = new BranchDAO();
            
            // Get sample riders and branches
            RiderInfo rider1 = riderDAO.getRiderByRiderId("RDR1234567");
            RiderInfo rider3 = riderDAO.getRiderByRiderId("RDR3456789");
            Branch branch1 = branchDAO.getBranchByCode("BR001");
            Branch branch2 = branchDAO.getBranchByCode("BR002");
            
            if (rider1 == null || rider3 == null || branch1 == null || branch2 == null) {
                System.out.println("Error: Riders or branches not found.");
                return;
            }
            
            // Insert sample pickups
            String sql = "INSERT INTO pickups (pickup_id, rider_id, branch_id, sender_name, " +
                         "sender_contact, pickup_address, package_description, package_weight, " +
                         "pickup_fee, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            // Pickup 1
            pstmt.setString(1, "PCK" + System.currentTimeMillis());
            pstmt.setInt(2, rider1.getId());
            pstmt.setInt(3, branch1.getId());
            pstmt.setString(4, "Deepak Sharma");
            pstmt.setString(5, "9804567890");
            pstmt.setString(6, "321 Elm St, Kathmandu");
            pstmt.setString(7, "Documents package");
            pstmt.setDouble(8, 0.5);
            pstmt.setDouble(9, 50.0);
            pstmt.setString(10, "Pending");
            pstmt.executeUpdate();
            
            // Pickup 2
            pstmt.setString(1, "PCK" + (System.currentTimeMillis() + 1));
            pstmt.setInt(2, rider3.getId());
            pstmt.setInt(3, branch2.getId());
            pstmt.setString(4, "Esha Magar");
            pstmt.setString(5, "9805678901");
            pstmt.setString(6, "654 Maple St, Lalitpur");
            pstmt.setString(7, "Gift package");
            pstmt.setDouble(8, 1.5);
            pstmt.setDouble(9, 120.0);
            pstmt.setString(10, "Completed");
            pstmt.executeUpdate();
            
            System.out.println("Sample pickups inserted successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error inserting sample pickups: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertSampleCashCollections() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if cash collections already exist
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM cash_collections");
            if (checkStmt.executeQuery().next() && checkStmt.executeQuery().getInt(1) > 0) {
                System.out.println("Cash collections already exist in the database.");
                return;
            }
            
            System.out.println("Inserting sample cash collection data...");
            
            // Get rider IDs
            RiderDAO riderDAO = new RiderDAO();
            
            // Get sample riders
            RiderInfo rider1 = riderDAO.getRiderByRiderId("RDR1234567");
            RiderInfo rider2 = riderDAO.getRiderByRiderId("RDR2345678");
            
            if (rider1 == null || rider2 == null) {
                System.out.println("Error: Riders not found.");
                return;
            }
            
            // Get delivery IDs
            PreparedStatement deliveryStmt = conn.prepareStatement(
                "SELECT id FROM deliveries WHERE status = 'Delivered' LIMIT 1");
            int deliveryId = -1;
            if (deliveryStmt.executeQuery().next()) {
                deliveryId = deliveryStmt.executeQuery().getInt(1);
            }
            
            // Insert sample cash collections
            String sql = "INSERT INTO cash_collections (collection_id, rider_id, delivery_id, " +
                         "amount_collected, collection_date, status, remarks) " +
                         "VALUES (?, ?, ?, ?, CURDATE(), ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            // Collection 1
            pstmt.setString(1, "COL" + System.currentTimeMillis());
            pstmt.setInt(2, rider1.getId());
            pstmt.setObject(3, deliveryId > 0 ? deliveryId : null);
            pstmt.setDouble(4, 1500.0);
            pstmt.setString(5, "Pending");
            pstmt.setString(6, "Cash collected for delivery");
            pstmt.executeUpdate();
            
            // Collection 2
            pstmt.setString(1, "COL" + (System.currentTimeMillis() + 1));
            pstmt.setInt(2, rider2.getId());
            pstmt.setObject(3, null);
            pstmt.setDouble(4, 2200.0);
            pstmt.setString(5, "Deposited");
            pstmt.setString(6, "Cash collected for multiple deliveries");
            pstmt.executeUpdate();
            
            System.out.println("Sample cash collections inserted successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error inserting sample cash collections: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertSampleIssues() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if issues already exist
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM reported_issues");
            if (checkStmt.executeQuery().next() && checkStmt.executeQuery().getInt(1) > 0) {
                System.out.println("Reported issues already exist in the database.");
                return;
            }
            
            System.out.println("Inserting sample issue data...");
            
            // Get rider IDs
            RiderDAO riderDAO = new RiderDAO();
            
            // Get sample riders
            RiderInfo rider1 = riderDAO.getRiderByRiderId("RDR1234567");
            RiderInfo rider3 = riderDAO.getRiderByRiderId("RDR3456789");
            
            if (rider1 == null || rider3 == null) {
                System.out.println("Error: Riders not found.");
                return;
            }
            
            // Get delivery and pickup IDs
            PreparedStatement deliveryStmt = conn.prepareStatement(
                "SELECT id FROM deliveries LIMIT 1");
            int deliveryId = -1;
            if (deliveryStmt.executeQuery().next()) {
                deliveryId = deliveryStmt.executeQuery().getInt(1);
            }
            
            PreparedStatement pickupStmt = conn.prepareStatement(
                "SELECT id FROM pickups LIMIT 1");
            int pickupId = -1;
            if (pickupStmt.executeQuery().next()) {
                pickupId = pickupStmt.executeQuery().getInt(1);
            }
            
            // Insert sample issues
            String sql = "INSERT INTO reported_issues (issue_id, rider_id, related_delivery_id, " +
                         "related_pickup_id, issue_type, issue_description, issue_date, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, CURDATE(), ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            // Issue 1
            pstmt.setString(1, "ISS" + System.currentTimeMillis());
            pstmt.setInt(2, rider1.getId());
            pstmt.setObject(3, deliveryId > 0 ? deliveryId : null);
            pstmt.setObject(4, null);
            pstmt.setString(5, "Delivery Problem");
            pstmt.setString(6, "Customer address was incorrect");
            pstmt.setString(7, "Open");
            pstmt.executeUpdate();
            
            // Issue 2
            pstmt.setString(1, "ISS" + (System.currentTimeMillis() + 1));
            pstmt.setInt(2, rider3.getId());
            pstmt.setObject(3, null);
            pstmt.setObject(4, pickupId > 0 ? pickupId : null);
            pstmt.setString(5, "Vehicle Issue");
            pstmt.setString(6, "Motorcycle had a flat tire during pickup");
            pstmt.setString(7, "Resolved");
            pstmt.executeUpdate();
            
            System.out.println("Sample reported issues inserted successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error inserting sample issues: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 