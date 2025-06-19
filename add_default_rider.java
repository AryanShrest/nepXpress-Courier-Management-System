import nepxpress.database.UserDAO;
import nepxpress.database.RiderDAO;

public class add_default_rider {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        RiderDAO riderDAO = new RiderDAO();
        String phone = "9814830243";
        String password = "12345678";
        String dob = "1990-01-01";
        String gender = "Male";
        String firstName = "Default";
        String surname = "Rider";
        String accountType = "Rider";
        String vehicleType = "Motorcycle";
        String licenseNumber = "RIDER1234";
        String vehicleRegistration = "BA-12-PA-1234";

        // Check if user exists
        if (!userDAO.emailOrMobileExists(phone)) {
            boolean created = userDAO.createUser(firstName, surname, phone, password, dob, gender, accountType);
            if (created) {
                int userId = userDAO.getUserIdByEmailOrMobile(phone);
                if (userId != -1) {
                    boolean riderCreated = riderDAO.createRider(userId, vehicleType, licenseNumber, vehicleRegistration);
                    if (riderCreated) {
                        System.out.println("Default rider account created successfully.");
                    } else {
                        System.out.println("Failed to create rider profile.");
                    }
                } else {
                    System.out.println("Failed to fetch user ID after creation.");
                }
            } else {
                System.out.println("Failed to create user.");
            }
        } else {
            System.out.println("Default rider already exists.");
        }
    }
}
