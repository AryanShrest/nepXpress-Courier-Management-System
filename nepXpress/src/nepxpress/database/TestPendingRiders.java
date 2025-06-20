package nepxpress.database;

import java.util.List;

public class TestPendingRiders {
    public static void main(String[] args) {
        RiderDAO dao = new RiderDAO();
        List<RiderInfo> pending = dao.getPendingRiders();
        System.out.println("Total pending riders: " + pending.size());
        for (RiderInfo rider : pending) {
            System.out.println("Rider: " + rider.getId() + ", " + rider.getFullName() + ", " + rider.getEmailOrMobile());
        }
    }
} 