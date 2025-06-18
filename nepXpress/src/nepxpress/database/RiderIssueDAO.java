package nepxpress.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RiderIssueDAO {
    private Connection connection;

    public RiderIssueDAO() {
        try {
            connection = DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean reportIssue(int riderId, String issueTitle, String issueDescription) {
        String sql = "INSERT INTO rider_issues (rider_id, issue_title, issue_description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, riderId);
            stmt.setString(2, issueTitle);
            stmt.setString(3, issueDescription);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<RiderIssue> getRiderIssues(int riderId) {
        List<RiderIssue> issues = new ArrayList<>();
        String sql = "SELECT * FROM rider_issues WHERE rider_id = ? ORDER BY reported_at DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, riderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RiderIssue issue = new RiderIssue();
                issue.setId(rs.getInt("id"));
                issue.setRiderId(rs.getInt("rider_id"));
                issue.setIssueTitle(rs.getString("issue_title"));
                issue.setIssueDescription(rs.getString("issue_description"));
                issue.setStatus(rs.getString("status"));
                issue.setReportedAt(rs.getTimestamp("reported_at"));
                issues.add(issue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }

    public boolean updateIssueStatus(int issueId, String status) {
        String sql = "UPDATE rider_issues SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, issueId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class RiderIssue {
        private int id;
        private int riderId;
        private String issueTitle;
        private String issueDescription;
        private String status;
        private Timestamp reportedAt;

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getRiderId() { return riderId; }
        public void setRiderId(int riderId) { this.riderId = riderId; }
        public String getIssueTitle() { return issueTitle; }
        public void setIssueTitle(String issueTitle) { this.issueTitle = issueTitle; }
        public String getIssueDescription() { return issueDescription; }
        public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Timestamp getReportedAt() { return reportedAt; }
        public void setReportedAt(Timestamp reportedAt) { this.reportedAt = reportedAt; }
    }
} 