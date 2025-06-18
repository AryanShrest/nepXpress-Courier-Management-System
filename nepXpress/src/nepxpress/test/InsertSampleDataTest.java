package nepxpress.test;

import nepxpress.dao.BranchDAO;
import nepxpress.dao.StaffDAO;
import nepxpress.model.Branch;
import nepxpress.model.Staff;

/**
 * Test class to insert sample branch and staff data into the database
 */
public class InsertSampleDataTest {

    public static void main(String[] args) {
        System.out.println("Starting sample data insertion...");
        
        // Insert sample branches
        insertSampleBranches();
        
        // Insert sample staff
        insertSampleStaff();
        
        System.out.println("Sample data insertion completed.");
    }
    
    private static void insertSampleBranches() {
        BranchDAO branchDAO = new BranchDAO();
        
        // Check if branches already exist
        if (branchDAO.getAllBranches().isEmpty()) {
            System.out.println("Inserting sample branch data...");
            
            // Create sample branches
            Branch branch1 = new Branch("BR001", "Kathmandu Branch", "Kathmandu, Nepal", 
                                       "01-4567890", "ktm@nepxpress.com");
            Branch branch2 = new Branch("BR002", "Pokhara Branch", "Pokhara, Nepal", 
                                       "061-467890", "pkr@nepxpress.com");
            Branch branch3 = new Branch("BR003", "Biratnagar Branch", "Biratnagar, Nepal", 
                                       "021-467890", "brt@nepxpress.com");
            
            // Insert branches
            int id1 = branchDAO.insertBranch(branch1);
            int id2 = branchDAO.insertBranch(branch2);
            int id3 = branchDAO.insertBranch(branch3);
            
            System.out.println("Inserted branches with IDs: " + id1 + ", " + id2 + ", " + id3);
        } else {
            System.out.println("Branches already exist in database, skipping insertion.");
        }
    }
    
    private static void insertSampleStaff() {
        StaffDAO staffDAO = new StaffDAO();
        BranchDAO branchDAO = new BranchDAO();
        
        // Check if staff already exist
        if (staffDAO.getAllStaff().isEmpty()) {
            System.out.println("Inserting sample staff data...");
            
            // Get branches
            Branch branch1 = branchDAO.getBranchByCode("BR001");
            Branch branch2 = branchDAO.getBranchByCode("BR002");
            Branch branch3 = branchDAO.getBranchByCode("BR003");
            
            if (branch1 == null || branch2 == null || branch3 == null) {
                System.out.println("Error: Branches not found. Please run insertSampleBranches first.");
                return;
            }
            
            // Create sample staff
            Staff staff1 = new Staff("STF1234567", branch1.getId(), "Anish", "Sharma", 
                                    "anish.sharma@nepxpress.com", "9801234567", "Branch Manager");
            
            Staff staff2 = new Staff("STF2345678", branch1.getId(), "Binita", "Thapa", 
                                    "binita.thapa@nepxpress.com", "9802345678", "Staff");
            
            Staff staff3 = new Staff("STF3456789", branch2.getId(), "Chetan", "Gurung", 
                                    "chetan.gurung@nepxpress.com", "9803456789", "Branch Manager");
            
            Staff staff4 = new Staff("STF4567890", branch3.getId(), "Deepa", "Rai", 
                                    "deepa.rai@nepxpress.com", "9804567890", "Branch Manager");
            
            // Insert staff
            int id1 = staffDAO.insertStaff(staff1);
            int id2 = staffDAO.insertStaff(staff2);
            int id3 = staffDAO.insertStaff(staff3);
            int id4 = staffDAO.insertStaff(staff4);
            
            System.out.println("Inserted staff with IDs: " + id1 + ", " + id2 + ", " + id3 + ", " + id4);
        } else {
            System.out.println("Staff already exist in database, skipping insertion.");
        }
    }
} 